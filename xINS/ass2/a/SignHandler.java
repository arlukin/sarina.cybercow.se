/*
 * @SignHandler.java 1.0 03/12/02
 *
 */

import java.security.*;
import java.io.*;

/*
 * Signerar en godtycklig fil med en privat nyckel.
 *
 * @version 	1.0 2 December 2003
 * @author 	Carina Nerén
 */
public class SignHandler {
	private String data1 = "";
	private String signature;
	private String data;
	private String key;
	private File f;

	/*
	 * Skapar en instans till klassen SignHandler och har som
	 * parametrar en referens till namnet på den fil som innehåller
	 * texten som ska signeras, namnet på den fil som innehåller den
	 * privata nyckeln samt en referens till namnet på den fil där
	 * signaturen ska sparas.
	 */
	SignHandler(String d, String k, String s) {
		data = d;
		key = k;
		signature = s;
		f = new File(data);

		try {
			FileOutputStream fos = new FileOutputStream(signature);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			FileInputStream fis = new FileInputStream(key);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			PrivateKey pk = (PrivateKey) o;
			System.out.println("DONE LOADING PRIVATEKEY");

			BufferedReader br = null;
			FileReader fr = new FileReader(f.getAbsolutePath());
			br = new BufferedReader(fr);
			String dataLine = br.readLine();
			while (dataLine != null) {
				String data2 = dataLine;
				data1 = data1.concat(data2);
				dataLine = br.readLine();
			}
			Signature signObj = Signature.getInstance("DSA");
			signObj.initSign(pk);
			signObj.update(data1.getBytes());
			oos.writeObject(signObj.sign());
		} catch (java.security.NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (java.security.InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (java.security.SignatureException se) {
			se.printStackTrace();
		} catch (java.io.FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (java.io.IOException ioe) {
			ioe.printStackTrace();
		} catch (java.lang.ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public static void main (String args[]) {
		String data = args[0];
		String key = args[1];
		String signature = args[2];
		new SignHandler(data, key, signature);

	}
}