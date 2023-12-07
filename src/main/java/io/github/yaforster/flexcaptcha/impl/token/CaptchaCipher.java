package io.github.yaforster.flexcaptcha.impl.token;

import io.github.yaforster.flexcaptcha.core.AbstractCaptchaCipher;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CaptchaCipher extends AbstractCaptchaCipher {

    @Builder
    private CaptchaCipher(CipherSettings cipherSettings, String encryptionPassword,
                          ExpirationTimeSettings expirationTimeSettings) {
        super(getCipherSettingsOrDefault(cipherSettings), getEncryptionPasswordOrDefault(encryptionPassword),
                expirationTimeSettings);
    }

    private static CipherSettings getCipherSettingsOrDefault(CipherSettings cipherSettings) {
        return Optional.ofNullable(cipherSettings).orElse(CipherSettings.getDefaultCipherSettings());
    }

    private static String getEncryptionPasswordOrDefault(String encryptionPassword) {
        return Optional.ofNullable(encryptionPassword).orElse(StringUtils.EMPTY);
    }

    @Override
    public final String generateToken(String captchaSolution, Serializable saltSource) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] ivBytes = generateIV().getIV();
            Cipher cipher = getCipher(encryptionPassword, saltSource, Cipher.ENCRYPT_MODE, ivBytes);
            byte[] cipherBytes = cipher.doFinal(captchaSolution.getBytes());
            outputStream.write(ivBytes);
            if (expirationTimeSettings != null) {
                appendExpirationDateBytes(cipher, outputStream);
            }
            outputStream.write(cipherBytes);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        }
        catch (Exception originalException) {
            throw mapEncryptionException(originalException);
        }
    }

    private void appendExpirationDateBytes(Cipher cipher, ByteArrayOutputStream outputStream) throws IOException {
        byte[] encryptedExpirationDateBytes = encryptExpirationDateBytes(cipher);
        outputStream.write(encryptedExpirationDateBytes);
    }

    private TokengenerationException mapEncryptionException(Exception originalException) {
        String errorMessage = switch (originalException) {
            case IllegalBlockSizeException ibse -> ibse.getLocalizedMessage();
            case BadPaddingException bpe ->
                    "Unknown padding specified for token encryption: " + bpe.getLocalizedMessage();
            default -> "Fatal error during cryptographic operation: " + originalException.getLocalizedMessage();
        };
        return new TokengenerationException(errorMessage, originalException);
    }

    private byte[] encryptExpirationDateBytes(Cipher cipher) {
        try {
            long expirationTime =
                    expirationTimeSettings.expirationTimeMillisOffset() + expirationTimeSettings.getTime();
            byte[] expirationDateBytes = BigInteger.valueOf(expirationTime).toByteArray();
            return cipher.doFinal(expirationDateBytes);
        }
        catch (GeneralSecurityException originalException) {
            throw mapEncryptionException(originalException);
        }
    }

    @Override
    public final boolean validateToken(String tokenString, Serializable saltSource, String userAnswer) {
        try {
            int ivBytesEnd = 16;
            int cipherBytesStartIndexInToken = 16;
            byte[] tokenbytes = Base64.getDecoder().decode(tokenString.getBytes());
            byte[] ivBytes = Arrays.copyOfRange(tokenbytes, 0, ivBytesEnd);
            Cipher cipher = getCipher(encryptionPassword, saltSource, Cipher.DECRYPT_MODE, ivBytes);
            if (expirationTimeSettings != null) {
                cipherBytesStartIndexInToken += 16;
                if (isTokenExpired(tokenbytes, cipherBytesStartIndexInToken, cipher)) {
                    return false;
                }
            }
            byte[] cipherBytes = Arrays.copyOfRange(tokenbytes, cipherBytesStartIndexInToken, tokenbytes.length);
            byte[] decryptedBytes = cipher.doFinal(cipherBytes);
            return userAnswer.equals(new String(decryptedBytes));
        }
        catch (Exception originalException) {
            throw mapEncryptionException(originalException);
        }
    }

    private boolean isTokenExpired(byte[] tokenbytes, int cipherBytesStartIndexInToken, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        long expirationTime = getDecryptedExpirationTimeMillis(tokenbytes, cipherBytesStartIndexInToken, cipher);
        long currentTimeMillis = expirationTimeSettings.getTime();
        return currentTimeMillis > expirationTime;
    }

    private long getDecryptedExpirationTimeMillis(byte[] tokenbytes, int cipherBytesStartIndexInToken, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] expirationTimeBytes = Arrays.copyOfRange(tokenbytes, 16, cipherBytesStartIndexInToken);
        byte[] decryptedExpirationTimeBytes = cipher.doFinal(expirationTimeBytes);
        return new BigInteger(decryptedExpirationTimeBytes).longValue();
    }
}
