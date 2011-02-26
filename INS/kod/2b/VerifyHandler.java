/*
 * @VerifyHandler.java 1.0 03/12/04
 *
 */

import java.security.*;
import java.io.*;

/*
 * Verifierar en godtycklig fil med en publik nyckel från ett certifikat samt signatur.
 *
 * @version 	1.0 4 December 2003
 * @author 	Carina Nerén
 */
public class VerifyHandler {
	private String certificate;
	private String data1 = "";
	private String signature;
	private String data;
	private File f;

	/*
	 * Skapar en instans av klassen VerifyHandler och har som
	 * parametrar en referens till namnet på den fil som innehåller
	 * texten som ska verifieras, namnet på den fil som innehåller
	 * certifikatet samt en referens till namnet på den fil som
	 * innehåller signaturen.
	 */
	public VerifyHandler(String d, String c, String s) {
		data = d;
		certificate = c;
		signature = s;
		f = new File(data);

		try {
			FileInputStream fisSign = new FileInputStream(signature);
			ObjectInputStream oisSign = new ObjectInputStream(fisSign);
			Object oSign = oisSign.readObject();
			if (!(oSign instanceof byte[])) {
				System.out.println("Unexepted data in file");
				System.exit(-1);
			}
			byte originalSignature[] = (byte []) oSign;

			BufferedReader br = null;
			FileReader fr = new FileReader(f.getAbsolutePath());
			br = new BufferedReader(fr);
			String dataLine = br.readLine();
			while (dataLine != null) {
				String data2 = dataLine;
				data1 = data1.concat(data2);
				dataLine = br.readLine();
			}

			FileInputStream fisCert = new FileInputStream(certificate);
			ObjectInputStream oisCert = new ObjectInputStream(fisCert);
			Object oCert = oisCert.readObject();
		    java.security.cert.Certificate cert = (java.security.cert.Certificate) oCert;
		    PublicKey pk = cert.getPublicKey();

			Signature sig = Signature.getInstance("DSA");
			sig.initVerify(pk);
			sig.update(data1.getBytes());
			if (sig.verify(originalSignature)) {
				System.out.println("MESSAGE VALID");
			} else {
				System.out.println("MESSAGE WAS CORRPTED");
			}
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
		String certificate = args[1];
		String signature = args[2];
		new VerifyHandler(data, certificate, signature);

	}
}
