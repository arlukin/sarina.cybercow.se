/*
 * @DecryptHandler.java 1.0 03/12/12
 *
 */

import java.security.spec.*;
import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.*;

/*
 * Avkrypterar data med en hemlig nyckel.
 *
 * @version 	1.0 12 December 2003
 * @author 	Carina Nerén
 */
public class DecryptHandler {
	private byte[] decryptedData;
	private Object encryptedData;
	private Object key;

	/*
	 * Skapar en instans av klassen DecryptHandler och har som
	 * parametrar en referens till den krypterade datan, samt
	 * en referens en hemlig nyckel.
	 */
	DecryptHandler(Object ed, Object k) {
		encryptedData = ed;
		key = k;

		try {
			byte[] encData = (byte []) encryptedData;
			SecretKey sk = (SecretKey) key;

			Cipher blowfishCipher = Cipher.getInstance("AES");
			System.out.println("DONE MAKING CIPHER ENGINE ");
			blowfishCipher.init(Cipher.DECRYPT_MODE, sk);
			System.out.println("DONE INITIALISING CIPHER ");
			decryptedData = blowfishCipher.doFinal(encData);
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



	/** Returner den avkrypterade datan. */
		public byte[] getDecryptedData() {
			return decryptedData;
	}



	public static void main (String args[]) {
		Object encryptedDataFile = args[0];
		Object key = args[1];
		new DecryptHandler(encryptedDataFile, key);
	}
}