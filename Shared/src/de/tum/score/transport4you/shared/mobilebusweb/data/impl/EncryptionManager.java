package de.tum.score.transport4you.shared.mobilebusweb.data.impl;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager {
	private final String password;
	private final Cipher cipher;
	private final SecretKey secret;

	public EncryptionManager() throws Exception {
		this.password = "MuchBusVeryTicket";
		// cipher still needs to be initialized before use according to
		// encryption/decryption!
		this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// derive a secret key from the password using PBKDF2, this.password and
		// an arbitrary salt
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(this.password.toCharArray(), "T4YSALTED".getBytes(), 1000,
				128);
		SecretKey tmp = factory.generateSecret(spec);
		this.secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	}

	public byte[] encryptSequence(byte[] sequence) throws Exception {
		// initialize cipher
		this.cipher.init(Cipher.ENCRYPT_MODE, this.secret, new SecureRandom());

		// because the IV was chosen at random, obtain it from the cipher
		byte[] iv = this.cipher.getIV();

		// do the encryption
		byte[] enc = this.cipher.doFinal(sequence);

		// store both IV and ciphertext in the result
		byte[] out = new byte[iv.length + enc.length];
		System.arraycopy(iv, 0, out, 0, iv.length);
		System.arraycopy(enc, 0, out, iv.length, enc.length);
		return out;
	}

	public byte[] decryptSequence(byte[] sequence) throws Exception {
		// obtain the IV from the sequence (first 16 byte)
		byte[] iv = Arrays.copyOfRange(sequence, 0, 16);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		this.cipher.init(Cipher.DECRYPT_MODE, this.secret, ivSpec);

		// obtain the ciphertext from the sequence (everything after the first
		// 16 bytes)
		byte[] enc = Arrays.copyOfRange(sequence, iv.length, sequence.length);

		// do the decryption
		return this.cipher.doFinal(enc);
	}

	public static void main(String[] args) throws Exception {
		EncryptionManager em = new EncryptionManager();
		System.out.println((byte) 0xAF);

		byte[] enc1 = em.encryptSequence(new byte[] { (byte) (0xAF), 0x32, 0x0, (byte) 0xFF });
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(enc1));
		System.out
				.println(javax.xml.bind.DatatypeConverter.printHexBinary(em.decryptSequence(enc1)));

		/*byte[] enc1 = em.encryptSequence("abcdefghijklmnopqrstuvwx".getBytes());
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(enc1));
		
		String dec1 = new String(em.decryptSequence(enc1));
		System.out.println(dec1);
		
		byte[] enc2 = em.encryptSequence("blablablabla".getBytes());
		System.out.println(javax.xml.bind.DatatypeConverter.printHexBinary(enc2));
		
		String dec2 = new String(em.decryptSequence(enc2));
		System.out.println(dec2);*/
	}
}
