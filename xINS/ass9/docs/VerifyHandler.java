/**
  * @VerifyHandler.java 1.0 03/12/12
  *
  */

import java.security.*;
import java.io.*;

/**
  * Verifierar en text med en publik nyckel samt signatur.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class VerifyHandler {
	private String messageString;
	private Object signature;
	private String data;
	private Object key;

	/**
	  * Skapar en instans av klassen VerifyHandler och har som
	  * parametrar en referens till en text, en publik nyckel samt
	  * en signatur.
	  */
	public VerifyHandler(String d, Object k, Object s) {
		data = d;
		key = k;
		signature = s;

		try {
			PublicKey pk = (PublicKey) key;
			byte originalSignature[] = (byte []) signature;
			Signature sig = Signature.getInstance("DSA");
			sig.initVerify(pk);
			sig.update(data.getBytes());
			if (sig.verify(originalSignature)) {
				messageString = "MESSAGE VALID";
			} else {
				messageString = "MESSAGE WAS CORRUPTED";
			}
		} catch (java.security.NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (java.security.InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (java.security.SignatureException se) {
			se.printStackTrace();
		}
	}



	/**
	  * Returnerar en sträng som talar om texten är
	  * valid eller inte.
	  */

	public String getMessageString() {
		return messageString;
	}

	public static void main (String args[]) {
		String data = args[0];
		Object key = args[1];
		Object signature = args[2];
		new VerifyHandler(data, key, signature);
	}
}
