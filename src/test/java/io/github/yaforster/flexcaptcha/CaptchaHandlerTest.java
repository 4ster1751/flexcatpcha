package io.github.yaforster.flexcaptcha;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Test;
import org.mockito.Mockito;

import javax.crypto.spec.IvParameterSpec;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Tests default methods of the {@link CaptchaHandler} interface using an
 * arbitrary implementation
 * <p>
 *
 * @author Yannick Forster
 */
public class CaptchaHandlerTest {

    final CipherHandler cipherHandler = getCHMock();
    final String password = "ThisIsMyPassword";
    final Button dummyObj = new Button();
    final CaptchaHandler captchaHandler = makeCaptchaHandler();

    @Test
    public void testAddSelfReference() {
        String token = captchaHandler.addSelfReference(cipherHandler, "sometoken", dummyObj, password);
        String selfReferenceBase64 = token.split(CaptchaHandler.DELIMITER)[1];
        assertEquals("AQID", selfReferenceBase64);
    }

    @Test
    public void testGetSaltObjectBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(dummyObj);
            baos.close();
            oos.close();
            byte[] testbytes = baos.toByteArray();
            byte[] methodTestBytes = captchaHandler.getSaltObjectBytes(dummyObj);
            assertArrayEquals(testbytes, methodTestBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * === Test setup ===
     */

    private CipherHandler getCHMock() {
        CipherHandler cipherHandler = Mockito.mock(CipherHandler.class);
        Mockito.when(cipherHandler.generateIV())
                .thenReturn(new IvParameterSpec(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16}));
        Mockito.when(cipherHandler.decryptString(any(byte[].class), anyString(), any()))
                .thenReturn(new byte[]{1, 2, 3});
        Mockito.when(cipherHandler.encryptString(any(byte[].class), anyString(), any(), any(byte[].class)))
                .thenReturn(new byte[]{1, 2, 3});
        return cipherHandler;

    }

    private CaptchaHandler makeCaptchaHandler() {
        return (answer, token, cipherHandler, saltSource, password) -> {
            throw new NotImplementedException();
        };
    }

}
