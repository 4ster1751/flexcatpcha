package io.github.yaforster.flexcaptcha.core;

import io.github.yaforster.flexcaptcha.impl.token.CaptchaCipher;
import io.github.yaforster.flexcaptcha.impl.token.CipherInstantiationException;
import io.github.yaforster.flexcaptcha.impl.token.CipherSettings;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AbstractCaptchaCipherTest {

    private static final String FICTIONAL_PASSWORD = "somepassword";
    private static final String FICTIONAL_SALT = "salt";
    private static final byte[] FICTIONAL_IV = {-71, 11, 45, -61, 44, -22, 5, -21, -91, -80, -121, -48, 29, -31, -25,
            76};

    @Test
    final void getCipher_shouldWork() {
        CaptchaCipher captchaCipher = CaptchaCipher.builder().build();
        assertDoesNotThrow(() -> captchaCipher.getCipher(FICTIONAL_PASSWORD, FICTIONAL_SALT, Cipher.ENCRYPT_MODE,
                FICTIONAL_IV));
    }

    @Test
    final void getCipher_should_Throw_and_map_SuchPaddingException() {
        try (MockedStatic<Cipher> cipherMockedStatic = Mockito.mockStatic(Cipher.class)) {
            cipherMockedStatic.when(() -> Cipher.getInstance(any())).thenThrow(NoSuchPaddingException.class);
            CaptchaCipher captchaCipher = CaptchaCipher.builder().build();
            CipherInstantiationException exception = assertThrows(CipherInstantiationException.class,
                    () -> captchaCipher.getCipher(FICTIONAL_PASSWORD, FICTIONAL_SALT, Cipher.ENCRYPT_MODE,
                            FICTIONAL_IV));
            assertTrue(exception.getLocalizedMessage().contains("Unknown padding specified for token encryption"));
        }
    }

    @Test
    final void getCipher_should_Throw_and_map_NoSuchAlgorithmException() {
        try (MockedStatic<Cipher> cipherMockedStatic = Mockito.mockStatic(Cipher.class)) {
            cipherMockedStatic.when(() -> Cipher.getInstance(any())).thenThrow(NoSuchAlgorithmException.class);
            CaptchaCipher captchaCipher = CaptchaCipher.builder().build();
            CipherInstantiationException exception = assertThrows(CipherInstantiationException.class,
                    () -> captchaCipher.getCipher(FICTIONAL_PASSWORD, FICTIONAL_SALT, Cipher.ENCRYPT_MODE,
                            FICTIONAL_IV));
            assertTrue(exception.getLocalizedMessage().contains("Unknown padding specified for token encryption"));
        }
    }

    @Test
    final void getCipher_should_Throw_and_map_anything_else() {
        CipherSettings invalidCipherSettings = new CipherSettings("PBKDF2WithHmacSHA256", "AES/CBC/PKCS5Padding",
                "somethingBroken");
        CaptchaCipher captchaCipher = CaptchaCipher.builder().cipherSettings(invalidCipherSettings).build();
        CipherInstantiationException exception = assertThrows(CipherInstantiationException.class,
                () -> captchaCipher.getCipher(FICTIONAL_PASSWORD, FICTIONAL_SALT, Cipher.ENCRYPT_MODE, FICTIONAL_IV));
        assertTrue(exception.getLocalizedMessage().contains("Fatal error during token encryption"));
    }
}