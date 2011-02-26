/**
  * @SecretKeyHandler.java 1.0 03/12/12
  *
  */

import java.security.spec.*;
import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import java.io.*;

/**
  * Generar en hemlig nyckel.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class SecretKeyHandler extends KeyPairGenerator {
	private SecureRandom random;
	private SecretKey secKey;
	private KeyGenerator kg;

	/**
	  * Skapar en instans av klassen KeyHandler som genererar en
	  * hemlig nyckel.
	  */
	SecretKeyHandler() {
		super("AES");

		try {
			kg = KeyGenerator.getInstance("AES");
			System.out.println("DONE MAKING KEY GENERATOR");
			kg.init(256, new SecureRandom());
			System.out.println("DONE MAKING KEY GENERATOR INITIALISATION");
			System.out.println("DONE MAKING KEY");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}



	/** Initialiserar generatorn till AES:s nyckel. */
	public void init(SecureRandom sr) {
		random = sr;
	}



	/** Returnerar den hemliga nyckeln. */
	public SecretKey getSecretKey() {
		secKey = kg.generateKey();
		return secKey;
	}



	public static void main(String[] args) throws Exception {
		new SecretKeyHandler();
	}
}