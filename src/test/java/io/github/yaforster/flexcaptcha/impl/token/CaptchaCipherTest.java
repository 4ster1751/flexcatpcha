package io.github.yaforster.flexcaptcha.impl.token;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Serializable;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CaptchaCipherTest {

    @Test
    void generateToken_without_expiration_time() {
        String solution = "abc123";
        Serializable salt = "SomeSalt";
        CaptchaCipher cipher = CaptchaCipher.builder().build();

        String generatedToken = cipher.generateToken(solution, salt);
        byte[] decodedToken = Base64.getDecoder().decode(generatedToken);

        assertEquals(32, decodedToken.length);
    }

    @Test
    void generateToken_expiration_time() {
        ExpirationTimeSettings mockedExpirationTime = Mockito.mock(ExpirationTimeSettings.class);
        when(mockedExpirationTime.getTime()).thenReturn(0L);
        when(mockedExpirationTime.expirationTimeMillisOffset()).thenReturn(1000L);
        String solution = "abc123";
        Serializable salt = "SomeSalt";
        CaptchaCipher cipher = CaptchaCipher.builder().expirationTimeSettings(mockedExpirationTime).build();

        String generatedToken = cipher.generateToken(solution, salt);
        byte[] decodedToken = Base64.getDecoder().decode(generatedToken);

        assertEquals(48, decodedToken.length);
    }

    @Test
    void generate_and_validate_Token_without_expiration_time() {
        String solution = "abc123";
        Serializable salt = "SomeSalt";
        CaptchaCipher cipher = CaptchaCipher.builder().build();

        String generatedToken = cipher.generateToken(solution, salt);
        boolean validationResult = cipher.validateToken(generatedToken, salt, solution);

        assertTrue(validationResult);
    }

    @Test
    void generate_and_validate_Token_with_expiration_time() {
        ExpirationTimeSettings mockedExpirationTime = Mockito.mock(ExpirationTimeSettings.class);
        when(mockedExpirationTime.getTime()).thenReturn(0L);
        when(mockedExpirationTime.expirationTimeMillisOffset()).thenReturn(1000L);
        String solution = "abc123";
        Serializable salt = "SomeSalt";
        CaptchaCipher cipher = CaptchaCipher.builder().expirationTimeSettings(mockedExpirationTime).build();

        String generatedToken = cipher.generateToken(solution, salt);

        when(mockedExpirationTime.getTime()).thenReturn(999L);
        boolean validationResult = cipher.validateToken(generatedToken, salt, solution);

        assertTrue(validationResult);
    }

    @Test
    void generate_and_validate_Token_with_expired_token() {
        ExpirationTimeSettings mockedExpirationTime = Mockito.mock(ExpirationTimeSettings.class);
        when(mockedExpirationTime.getTime()).thenReturn(0L);
        when(mockedExpirationTime.expirationTimeMillisOffset()).thenReturn(1000L);
        String solution = "abc123";
        Serializable salt = "SomeSalt";
        CaptchaCipher cipher = CaptchaCipher.builder().expirationTimeSettings(mockedExpirationTime).build();

        String generatedToken = cipher.generateToken(solution, salt);

        when(mockedExpirationTime.getTime()).thenReturn(1001L);
        boolean validationResult = cipher.validateToken(generatedToken, salt, solution);

        assertFalse(validationResult);
    }

    @Test
    void generateToken_test_error_handling() {
        CaptchaCipher cipher = CaptchaCipher.builder().build();

        TokengenerationException exception = assertThrows(TokengenerationException.class,
                () -> cipher.generateToken(null, null));
        assertTrue(exception.getLocalizedMessage().contains("Fatal error during cryptographic operation"));
    }

}