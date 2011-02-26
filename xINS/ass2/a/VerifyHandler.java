/*
 * @VerifyHandler.java 1.0 03/12/02
 *
 */

import java.security.*;
import java.io.*;

/*
 * Verifierar en godtycklig fil med en publik nyckel samt signatur.
 *
 * @version 	1.0 2 December 2003
 * @author 	Carina Nerén
 */
public class VerifyHandler {
	private String data1 = "";
	private String signature;
	private String data;
	private String key;
	private File f;

	/*
	 * Skapar en instans av klassen VerifyHandler och har som
	 * parametrar en referens till namnet på den fil som innehåller
	 * texten som ska verifieras, namnet på den fil som innehåller den
	 * publika nyckeln samt en referens till namnet på den fil som
	 * innehåller signaturen.
	 */
	public VerifyHandler(String d, String k, String s) {
		data = d;
		key = k;
		signature = s;
		f = new File(data);

		try {
			FileInputStream fis2 = new FileInputStream(key);
			ObjectInputStream ois2 = new ObjectInputStream(fis2);
			Object o2 = ois2.readObject();
			PublicKey pk = (PublicKey) o2;

			FileInputStream fis = new FileInputStream(signature);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			if (!(o instanceof byte[])) {
				System.out.println("Unexepted data in file");
				System.exit(-1);
			}
			byte originalSignature[] = (byte []) o;

			BufferedReader br = null;
			FileReader fr = new FileReader(f.getAbsolutePath());
			br = new BufferedReader(fr);
			String dataLine = br.readLine();
			while (dataLine != null) {
				String data2 = dataLine;
				data1 = data1.concat(data2);
				dataLine = br.readLine();
			}
			Signature sig = Signature.getInstance("DSA");
			sig.initVerify(pk);
			sig.update(data1.getBytes());
			if (sig.verify(originalSignature)) {
				System.out.println("MESSAGE VALID");
			} else
				System.out.println("MESSAGE WAS CORRPTED");
		} catch (java.security.NoSuchAlgorithmException nsae) {
		} catch (java.io.FileNotFoundException fnfe) {
		} catch (java.io.IOException ioe) {
		} catch (java.lang.ClassNotFoundException cnfe) {
		} catch (java.security.InvalidKeyException ike) {
		} catch (java.security.SignatureException se) {
		}
	}

	public static void main (String args[]) {
		String data = args[0];
		String key = args[1];
		String signature = args[2];
		new VerifyHandler(data, key, signature);

	}
}
