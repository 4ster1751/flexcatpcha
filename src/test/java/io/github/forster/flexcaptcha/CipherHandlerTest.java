package io.github.forster.flexcaptcha;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.Button;
import java.util.Arrays;

import org.junit.Test;

public class CipherHandlerTest {

	CipherHandler ch = new CipherHandler();
	byte[] inputBytes = "TestString".getBytes();
	String password = "ThisIsMyPassword";
	Button dummyObj = new Button();
	byte[] ivBytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

	byte[] encrExpected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, -32, 13, -33, -117, 18,
			92, 6, 17, -66, -63, -118, 122, -18, 119, -57, -13 };
	byte[] decrExpected = new byte[] { 84, 101, 115, 116, 83, 116, 114, 105, 110, 103 };
	byte[] encrNoSaltSourceExpected = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, -80, -47, -48,
			81, 125, 62, 101, 85, -9, 21, -103, -91, 94, 95, 80, 88 };
	byte[] decrNoSaltSourceExpected = new byte[] { 84, 101, 115, 116, 83, 116, 114, 105, 110, 103 };

	@Test
	public void testEncryptString_ShouldWork() {
		byte[] encrypted = ch.encryptString(inputBytes, password, dummyObj, ivBytes);
		assertTrue(Arrays.equals(encrypted, encrExpected));
	}

	@Test
	public void testEncryptString_NoInput_ShouldFail() {
		assertThrows(IllegalArgumentException.class, () -> {
			ch.encryptString(null, password, dummyObj, ivBytes);
		});
	}

	@Test
	public void testEncryptString_NoPassword_ShouldFail() {
		assertThrows(IllegalArgumentException.class, () -> {
			ch.encryptString(inputBytes, null, dummyObj, ivBytes);
		});
	}

	@Test
	public void testEncryptString_NoSaltSource_ShouldWork() {
		byte[] encrypted = ch.encryptString(inputBytes, password, null, ivBytes);
		assertTrue(encrypted.length == 32);
	}

	@Test
	public void testEncryptString_EmptySaltSource_ShouldWork() {
		byte[] encrypted = ch.encryptString(inputBytes, password, "", ivBytes);
		assertTrue(encrypted.length == 32);
	}

	@Test
	public void testEncryptString_NullIVBytes_ShouldFail() {
		assertThrows(NullPointerException.class, () -> {
			ch.encryptString(inputBytes, password, "", null);
		});
	}

	@Test
	public void testEncryptString_NoIVBytes_ShouldWork() {
		byte[] encrypted = ch.encryptString(inputBytes, password, "");
		assertTrue(encrypted.length == 32);
	}

	@Test
	public void testDecryptString_ShouldWork() {
		byte[] decrypted = ch.decryptString(encrExpected, password, dummyObj);
		assertTrue(Arrays.equals(decrypted, decrExpected));
	}

	@Test
	public void testDecryptString_NoInput_ShouldFail() {
		assertThrows(NullPointerException.class, () -> {
			ch.decryptString(null, password, dummyObj);
		});
	}

	@Test
	public void testDecryptString_NoPassword_ShouldFail() {
		assertThrows(IllegalArgumentException.class, () -> {
			ch.decryptString(inputBytes, null, dummyObj);
		});
	}

	@Test
	public void testDecryptString_NoSaltSource_ShouldWork() {
		byte[] decrypted = ch.decryptString(encrNoSaltSourceExpected, password, null);
		assertTrue(Arrays.equals(decrypted, decrNoSaltSourceExpected));
	}

	@Test
	public void testDecryptString_EmptySaltSource_ShouldFail() {
		byte[] decrypted = ch.decryptString(encrNoSaltSourceExpected, password, "");
		assertTrue(decrypted == null);
	}

	@Test
	public void testGenerateIV_ShouldWork() {
		byte[] iv = ch.generateIV().getIV();
		assertTrue(iv.length==16);
	}

}
