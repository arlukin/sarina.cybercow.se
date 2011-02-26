/**
  * @KeyHandler.java 1.0 03/12/12
  *
  */

import java.security.*;
import java.io.*;

/**
  * Generar ett nyckelpar med en publik samt en privat nyckel.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class KeyHandler extends KeyPairGenerator {
	private SecureRandom random;
	private PrivateKey privKey;
	private PublicKey pubKey;
	private KeyPair kp;

	/** Skapar en instans av klassen KeyHandler. */
	KeyHandler() {
		super("DSA");

		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
			System.out.println("DONE MAKING KEY GENERATOR");
			kpg.initialize(512, new SecureRandom());
			System.out.println("DONE MAKING KEY GENERATOR INITIALISATION");
			kp = kpg.generateKeyPair();
			System.out.println("DONE MAKING KEYPAIR");
		} catch(NoSuchAlgorithmException e) {
		}
	}



	/** Returnerar den publika nyckeln. */
	public PublicKey getPublicKey() {
		pubKey = kp.getPublic();
		return pubKey;
	}



	/** Returnerar den privata nyckeln. */
	public PrivateKey getPrivateKey() {
		privKey = kp.getPrivate();
		return privKey;
	}



	/** Initialiserar generatorn till DSA:s nyckelpar. */
	public void initialize(int strength, SecureRandom sr) {
		random = sr;
	}



	public static void main(String[] args) throws Exception {
		new KeyHandler();
	}
}