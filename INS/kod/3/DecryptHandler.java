/*
 * @DecryptHandler.java 1.0 03/12/04
 *
 */

import java.security.spec.*;
import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.*;

/*
 * Avkrypterar en godtycklig fil med en hemlig nyckel samt sparar det på fil.
 *
 * @version 	1.0 4 December 2003
 * @author 	Carina Nerén
 */
public class DecryptHandler {
	private String encryptedData = "";
	private String encryptedDataFile;
	private String decryptedDataFile;
	private byte[] decryptedFile;
	private String key;
	private File f2;
	private File f;

	/*
	 * Skapar en instans av klassen DecryptHandler och har som
	 * parametrar en referens till namnet på den fil som innehåller
	 * den krypterade datan, namnet på den fil som innehåller den
	 * hemliga nyckeln, samt en referens till namnet på den fil där
	 * den avkrypterade datan sparas.
	 */
	DecryptHandler(String edf, String k, String ddf) {
		encryptedDataFile = edf;
		key = k;
		decryptedDataFile = ddf;
		byte[] decryptedFile;
		f = new File(encryptedDataFile);
		f2 = new File(decryptedDataFile);

		try {
			FileOutputStream fos = new FileOutputStream(f2);
			DataOutputStream dos = new DataOutputStream(fos);

			FileInputStream fis = new FileInputStream(key);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			SecretKey sk = (SecretKey) o;
			System.out.println("DONE LOADING SECRETKEY ");

			DataInputStream dos2 = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
			int length = dos2.available();
			byte[] fileBytes = new byte[length];
			dos2.readFully(fileBytes);

			Cipher blowfishCipher = Cipher.getInstance("AES");
			System.out.println("DONE MAKING CIPHER ENGINE ");
			blowfishCipher.init(Cipher.DECRYPT_MODE, sk);
			System.out.println("DONE INITIALISING CIPHER ");
			decryptedFile = blowfishCipher.doFinal(fileBytes);
			for (int i=0; i<decryptedFile.length; i++) {
				dos.write(decryptedFile[i]);
			}
			System.out.println("DONE DELEVIRING DATA TO CIPHER ENGINGE & SAVING PLAIN-TEXT DATA ");
		} catch (java.security.NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (java.security.InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (java.io.FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		} catch (java.lang.ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (javax.crypto.BadPaddingException bpe) {
			bpe.printStackTrace();
		}
	}



	public static void main (String args[]) {
		String encryptedDataFile = args[0];
		String key = args[1];
		String decryptedDataFile = args[2];
		new DecryptHandler(encryptedDataFile, key, decryptedDataFile);
	}
}