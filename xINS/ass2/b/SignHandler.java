/*
 * @SignHandler.java 1.0 03/12/03
 *
 */

import java.security.*;
import java.io.*;

/*
 * Signerar en godtycklig fil med en privat nyckel.
 *
 * @version 	1.0 3 December 2003
 * @author 	Carina Nerén
 */
public class SignHandler {
	private String keyStorePassword;
	private String keyPassword;
	private String certificate;
	private String data1 = "";
	private String signature;
	private String keyStore;
	private File certFile;
	private String alias;
	private String data2;
	private String data;
	private File f;

	/*
	 * Skapar en instans till klassen SignHandler och har som
	 * parametrar en referens till namnet på den fil som innehåller
	 * nyckelparet, lösenordet till nyckelparet, namnet på den privata
	 * nyckeln, lösenord till nyckeln, namnet på den fil som innehåller
	 * texten som ska signeras, namnet på den fil där certifikatet ska
	 * sparas samt en referens till namnet på den fil där signaturen ska sparas.
	 */
	SignHandler(String ks, String ksp, String a, String kp, String d, String c, String s) {
		keyStore = ks;
		keyStorePassword = ksp;
		alias = a;
		keyPassword = kp;
		data = d;
		certificate = c;
		signature = s;
		f = new File(data);

		try {
			FileOutputStream fos = new FileOutputStream(signature);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);

		    FileOutputStream fos2 = new FileOutputStream(certificate);
		    ObjectOutputStream oos2 = new ObjectOutputStream(fos2);

		    BufferedReader br = null;
			FileReader fr = new FileReader(f.getAbsolutePath());
			br = new BufferedReader(fr);
			String dataLine = br.readLine();
			while (dataLine != null) {
				String data2 = dataLine;
				data1 = data1.concat(data2);
				dataLine = br.readLine();

			}

		    FileInputStream fis = new FileInputStream(keyStore);
		    KeyStore ks2 = KeyStore.getInstance(KeyStore.getDefaultType());
		    char [] ch = keyStorePassword.toCharArray();
		    char [] ch2 = keyPassword.toCharArray();
		    ks2.load(fis, ch);
		    PrivateKey pk = (PrivateKey) ks2.getKey(alias, ch2);

		    java.security.cert.Certificate cer = ks2.getCertificate(alias);

		    Signature signObj = Signature.getInstance("DSA");
			signObj.initSign(pk);
			signObj.update(data1.getBytes());
			oos.writeObject(signObj.sign());
			oos2.writeObject(cer);
		} catch (java.security.cert.CertificateException ce) {
			ce.printStackTrace();
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
		} catch (java.security.KeyStoreException kse) {
			kse.printStackTrace();
		} catch (java.security.UnrecoverableKeyException uke) {
			uke.printStackTrace();
		}
	}

	public static void main (String args[]) {
		String keyStore = args[0];
		String keyStorePassword = args[1];
		String alias = args[2];
		String keyPassword = args[3];
		String data = args[4];
		String certificate = args[5];
		String signature = args[6];
		new SignHandler(keyStore, keyStorePassword, alias, keyPassword, data, certificate, signature);

	}
}