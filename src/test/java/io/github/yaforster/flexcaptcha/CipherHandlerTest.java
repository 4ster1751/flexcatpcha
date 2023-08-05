package io.github.yaforster.flexcaptcha;

import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class CipherHandlerTest {

    private final CipherHandler ch = new CipherHandler();
    private final byte[] inputBytes = "TestString".getBytes();
    private final String password = "ThisIsMyPassword";
    private final Button dummyObj = new Button();
    private final byte[] ivBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    private final byte[] encrExpected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, -32, 13, -33, -117, 18,
            92, 6, 17, -66, -63, -118, 122, -18, 119, -57, -13};
    private final byte[] decrExpected = new byte[]{84, 101, 115, 116, 83, 116, 114, 105, 110, 103};
    private final byte[] encrNoSaltSourceExpected = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, -80, -47, -48,
            81, 125, 62, 101, 85, -9, 21, -103, -91, 94, 95, 80, 88};
    private final byte[] decrNoSaltSourceExpected = new byte[]{84, 101, 115, 116, 83, 116, 114, 105, 110, 103};

    @Test
    public void testEncryptString_ShouldWork() {
        byte[] encrypted = ch.encryptString(inputBytes, password, dummyObj, ivBytes);
        assertArrayEquals(encrypted, encrExpected);
    }

    @Test
    public void testEncryptString_NoInput_ShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> ch.encryptString(null, password, dummyObj, ivBytes));
    }

    @Test
    public void testEncryptString_NoPassword_ShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> ch.encryptString(inputBytes, null, dummyObj, ivBytes));
    }

    @Test
    public void testEncryptString_NoSaltSource_ShouldWork() {
        byte[] encrypted = ch.encryptString(inputBytes, password, null, ivBytes);
        assertEquals(32, encrypted.length);
    }

    @Test
    public void testEncryptString_EmptySaltSource_ShouldWork() {
        byte[] encrypted = ch.encryptString(inputBytes, password, "", ivBytes);
        assertEquals(32, encrypted.length);
    }

    @Test
    public void testEncryptString_NullIVBytes_ShouldFail() {
        assertThrows(NullPointerException.class, () -> ch.encryptString(inputBytes, password, "", null));
    }

    @Test
    public void testEncryptString_NoIVBytes_ShouldWork() {
        byte[] encrypted = ch.encryptString(inputBytes, password, "");
        assertEquals(32, encrypted.length);
    }

    @Test
    public void testDecryptString_ShouldWork() {
        byte[] decrypted = ch.decryptString(encrExpected, password, dummyObj);
        assertArrayEquals(decrypted, decrExpected);
    }

    @Test
    public void testDecryptString_NoInput_ShouldFail() {
        assertThrows(NullPointerException.class, () -> ch.decryptString(null, password, dummyObj));
    }

    @Test
    public void testDecryptString_NoPassword_ShouldFail() {
        assertThrows(IllegalArgumentException.class, () -> ch.decryptString(inputBytes, null, dummyObj));
    }

    @Test
    public void testDecryptString_NoSaltSource_ShouldWork() {
        byte[] decrypted = ch.decryptString(encrNoSaltSourceExpected, password, null);
        assertArrayEquals(decrypted, decrNoSaltSourceExpected);
    }

    @Test
    public void testDecryptString_EmptySaltSource_ShouldFail() {
        byte[] decrypted = ch.decryptString(encrNoSaltSourceExpected, password, "");
        assertNull(decrypted);
    }

    @Test
    public void testGenerateIV_ShouldWork() {
        byte[] iv = new CipherHandler().generateIV().getIV();
        assertEquals(16, iv.length);
    }

}
