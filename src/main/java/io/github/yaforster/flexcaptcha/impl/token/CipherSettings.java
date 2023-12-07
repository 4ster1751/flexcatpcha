package io.github.yaforster.flexcaptcha.impl.token;

public record CipherSettings(String encryptionAlgorithm, String cipherAlgorithm, String secretKeySpecAlgorithm) {
    public static CipherSettings getDefaultCipherSettings() {
        final String DEFAULT_ENCRYPTION_ALGORITHM = "PBKDF2WithHmacSHA256";
        final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
        final String DEFAULT_SECRETKEYSPEC_ALGORITHM = "AES";
        return new CipherSettings(DEFAULT_ENCRYPTION_ALGORITHM, DEFAULT_CIPHER_ALGORITHM,
                DEFAULT_SECRETKEYSPEC_ALGORITHM);
    }
}
