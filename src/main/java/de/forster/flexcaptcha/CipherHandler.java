package de.forster.flexcaptcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Handles String encryption or decryption.
 * 
 * @author Yannick Forster
 *
 */
public class CipherHandler {
	
	Logger log = LogManager.getLogger(CipherHandler.class);
	
	static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	static final String CIPERALGORITHM = "AES/CBC/PKCS5Padding";
	static final String AES = "AES";
	
	/**
	 * Encrypts a given String with a password and a salt source. To encrypt it, an
	 * initialization vector is generated and used to encrypt the string. The
	 * initialization vector is then put in front of the encrypted byte array for
	 * transportation, so it can be used to decrypt the byte array after it (using
	 * the same password and salt) at a later point.
	 * 
	 * @param input      the input byte array to be encrypted
	 * @param password   the password used for encryption
	 * @param saltSource a Serializable object used as salt
	 * @return the encrypted string
	 */
	public byte[] encryptString(byte[] input, String password, Serializable saltSource, byte[] ivBytes) {
		try {
			Cipher cipher = getCipher(password, saltSource, Cipher.ENCRYPT_MODE, ivBytes);
			byte[] cipherBytes = cipher.doFinal(input);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write( ivBytes );
			outputStream.write( cipherBytes );
			byte[] finalTokenBytes = outputStream.toByteArray( );
			return finalTokenBytes;
		} catch (IOException e) {
			log.fatal("Fatal error producing byte array data of the salt source object: " + e.getLocalizedMessage());
		} catch (NoSuchAlgorithmException e) {
			log.fatal("Unknown algorithm specified for token encryption: " + e.getLocalizedMessage());
		} catch (NoSuchPaddingException e) {
			log.fatal("Unknown padding specified for token encryption: " + e.getLocalizedMessage());
		} catch (InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			log.fatal("Fatal error during token encryption: " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Decrypts an encrypted string using a specified password and salt source
	 * object. The decrypted string must contain the initialization vector as the
	 * first 16 bytes as they will be extracted to be used in the decryption.
	 * 
	 * @param input      the input byte array to be decrypted
	 * @param password   the password used for encryption
	 * @param saltSource a Serializable object used as salt
	 * @return the decrypted string
	 */
	public byte[] decryptString(byte[] input, String password, Serializable saltSource) {
		try {
			byte[] ivBytes = Arrays.copyOfRange( input  , 0, 16);
			Cipher cipher = getCipher(password, saltSource, Cipher.DECRYPT_MODE, ivBytes);
			byte[] cipherBytes = Arrays.copyOfRange( input  , 16, input.length);
		    byte[] inputBytes = cipher.doFinal(cipherBytes);
		    return inputBytes;
		} catch (IOException e) {
			log.fatal("Fatal error producing byte array data of the salt source object: " + e.getLocalizedMessage());
		} catch (NoSuchAlgorithmException e) {
			log.fatal("Unknown algorithm specified for token decryption: " + e.getLocalizedMessage());
		} catch (NoSuchPaddingException e) {
			log.fatal("Unknown padding specified for token decryption: " + e.getLocalizedMessage());
		} catch (InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			log.fatal("Fatal error during token decryption: " + e.getLocalizedMessage());
		}
		return null;
	}
	
	/**
	 * Generates and configures the {@link Cipher} object used for encryption and
	 * decryption.
	 * 
	 * @param password   the password used for encryption
	 * @param saltSource a Serializable object used as salt
	 * @param mode       specifies whether or not the cipher will encrypt or decrypt
	 * @param ivBytes    the initialization vector
	 * @return configured Cipher object
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private Cipher getCipher(String password, Serializable saltSource, int mode, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeySpecException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		IvParameterSpec iv = new IvParameterSpec(ivBytes);
		SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
		Cipher cipher = Cipher.getInstance(CIPERALGORITHM);
		byte[] saltBytes = getSaltBytes(saltSource);
		KeySpec ks = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
		SecretKey key = new SecretKeySpec(factory.generateSecret(ks).getEncoded(), AES);
		cipher.init(mode, key, iv);
		return cipher;
	}

	/**
	 * Gets the byte array of the salt source object
	 * 
	 * @param saltSource object to be used as salt
	 * @return byte array of the given object
	 * @throws IOException
	 */
	private byte[] getSaltBytes(Serializable saltSource) throws IOException {
		byte[] saltBytes;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		out = new ObjectOutputStream(bos);   
		out.writeObject(saltSource);
		out.flush();
		saltBytes = bos.toByteArray();
		return saltBytes;
	}
	
	/**
	 * generates a new initialization vector as randomized 16 bytes and returns it
	 * as {@link IvParameterSpec}
	 * 
	 * @return randomized {@link IvParameterSpec}
	 */
	public IvParameterSpec generateIV() {
	    byte[] iv = new byte[16];
	    new SecureRandom().nextBytes(iv);
	    return new IvParameterSpec(iv);
	}

}
