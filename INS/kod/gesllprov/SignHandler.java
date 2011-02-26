/**
  * @SignHandler.java 1.0 03/12/12
  *
  */

import java.security.*;
import java.io.*;

/**
  * Signerar en text med en privat nyckel.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class SignHandler {
	private byte[] messageSignature;
	private String signature;
	private String data;
	private Object key;

	/**
	  * Skapar en instans av klassen SignHandler och har som
	  * parametrar en referens till en text och en privatnyckel.
	  */
	SignHandler(String d, Object k) {
		data = d;
		key = k;

		try {
			PrivateKey privKey = (PrivateKey) key;
			Signature signObj = Signature.getInstance("DSA");
			signObj.initSign(privKey);
			signObj.update(data.getBytes());
			messageSignature = signObj.sign();
		} catch (java.security.NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (java.security.InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (java.security.SignatureException se) {
			se.printStackTrace();
		}
	}



	/** Returner signaturen. */
	public byte[] getMessageSignature() {
		return messageSignature;

	}

	public static void main (String args[]) {
		String data = args[0];
		Object key = args[1];
		new SignHandler(data, key);
	}
}