package io.github.yaforster.flexcaptcha.core;

import io.github.yaforster.flexcaptcha.impl.token.CipherInstantiationException;
import io.github.yaforster.flexcaptcha.impl.token.CipherSettings;
import io.github.yaforster.flexcaptcha.impl.token.ExpirationTimeSettings;
import io.github.yaforster.flexcaptcha.impl.token.TokengenerationException;
import lombok.AllArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

@AllArgsConstructor
public abstract class AbstractCaptchaCipher {

    protected CipherSettings cipherSettings;
    protected String encryptionPassword;
    protected ExpirationTimeSettings expirationTimeSettings;

    /**
     * generates a new initialization vector as randomized 16 bytes and returns it as
     * {@link IvParameterSpec}
     *
     * @return randomized {@link IvParameterSpec}
     */
    public static IvParameterSpec generateIV() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * Generates and configures the {@link Cipher} object used for encryption and decryption.
     *
     * @param password   the password used for encryption
     * @param saltSource a Serializable object used as salt
     * @param mode       specifies whether the cipher will encrypt or decrypt
     * @param ivBytes    the initialization vector
     * @return configured Cipher object
     */
    protected final Cipher getCipher(final String password, final Serializable saltSource, final int mode,
                                     final byte[] ivBytes) {
        try {
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(cipherSettings.encryptionAlgorithm());
            Cipher cipher = Cipher.getInstance(cipherSettings.cipherAlgorithm());
            byte[] saltBytes = getSaltBytes(saltSource);
            KeySpec ks = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
            SecretKey key = new SecretKeySpec(factory.generateSecret(ks)
                    .getEncoded(), cipherSettings.secretKeySpecAlgorithm());
            cipher.init(mode, key, iv);
            return cipher;
        }
        catch (GeneralSecurityException originalException) {
            throw mapCipherGenerationException(originalException);
        }
    }

    /**
     * Gets the byte array of the salt source object
     *
     * @param saltSource object to be used as salt
     * @return byte array of the given object
     */
    public static byte[] getSaltBytes(final Serializable saltSource) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream out =
                new ObjectOutputStream(bos)) {
            out.writeObject(saltSource);
            return bos.toByteArray();
        }
        catch (IOException e) {
            throw new TokengenerationException("Fatal error during convertion of salt into byte array.");
        }
    }

    private CipherInstantiationException mapCipherGenerationException(GeneralSecurityException originalException) {
        String errorMessage = switch (originalException) {
            case NoSuchPaddingException nspe ->
                    "Unknown padding specified for token encryption: " + nspe.getLocalizedMessage();
            case NoSuchAlgorithmException nsae ->
                    "Unknown padding specified for token encryption: " + nsae.getLocalizedMessage();
            default -> "Fatal error during token encryption: " + originalException.getLocalizedMessage();
        };
        return new CipherInstantiationException(errorMessage, originalException);
    }

    /**
     * Generates a token out of the given captcha solution and another object acting as the salt.
     *
     * @param captchaSolution the actual captcha solution.
     * @param saltSource      a {@link Serializable}
     * @return the generated token to be returned later for validation alongside the user input
     */
    public abstract String generateToken(final String captchaSolution, final Serializable saltSource);

    /**
     * Validates the user input to solve the captcha against the returned token
     *
     * @param tokenString the returned token generated with the original captcha
     * @param saltSource  the salt source used to generate the original captcha
     * @param answer      the given answer to be validated
     * @return boolean of the validation result.
     */
    public abstract boolean validateToken(final String tokenString, final Serializable saltSource, final String answer);
}
