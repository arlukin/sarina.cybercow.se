/*
 * @EncryptHandler.java 1.0 03/12/12
 *
 */

import java.security.spec.*;
import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.*;

/*
 * Krypterar data med en hemlig nyckel.
 *
 * @version 	1.0 12 December 2003
 * @author 	Carina Nerén
 */
public class EncryptHandler {
	private byte[] encryptedData;
	private Object data;
	private Object key;

	/*
	 * Skapar en instans av klassen EncryptHandler och har som
	 * parametrar en referens till datan som ska krypteras samt
	 * en hemlig nyckel.
	 */
	EncryptHandler(Object d, Object k) {
		data = d;
		key = k;

		try {
			byte[] dataBytes = (byte []) data;
			SecretKey sk = (SecretKey) key;

			Cipher blowfishCipher = Cipher.getInstance("AES");
			System.out.println("DONE MAKING CIPHER ENGINE ");
			blowfishCipher.init(Cipher.ENCRYPT_MODE, sk);
			System.out.println("DONE INITIALISING CIPHER ");
			encryptedData = blowfishCipher.doFinal(dataBytes);
			System.out.println("DONE DELEVIRING DATA TO CIPHER ENGINGE");
		} catch (java.security.NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (java.security.InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (javax.crypto.BadPaddingException bpe) {
			bpe.printStackTrace();
		}
	}



	/** Returner den krypterade datan. */
	public byte[] getEncryptedData() {
		return encryptedData;
	}



	public static void main (String args[]) {
		Object data = args[0];
		Object key = args[1];
		new EncryptHandler(data, key);
	}
}