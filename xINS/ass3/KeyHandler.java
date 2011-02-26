/*
 * @KeyHandler.java 1.0 03/12/04
 *
 */

import java.security.spec.*;
import java.security.*;
import javax.crypto.spec.*;
import javax.crypto.*;
import java.io.*;

/*
 * Generar en hemlig nyckel samt spara denna i en godtycklig fil.
 *
 * @version 	1.0 4 December 2003
 * @author 	Carina Nerén
 */
public class KeyHandler extends KeyPairGenerator {
	private SecureRandom random;
	private SecretKey sk;
	private String file1;

	/*
	 * Skapar en instans av klassen KeyHandler och har som
	 * parameter en referens till namnet på den fil som nyckeln
	 * ska sparas på.
	 */
	KeyHandler(String f1) {
		super("AES");
		file1 = f1;

		try {
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			System.out.println("DONE MAKING KEY GENERATOR");
			kg.init(256, new SecureRandom());
			System.out.println("DONE MAKING KEY GENERATOR INITIALISATION");
			sk = kg.generateKey();
			System.out.println("DONE MAKING KEY");
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		saveKey(sk, file1);
		System.out.println("DONE SAVING KEY TO FILE " + file1);
	}



	/*Initialiserar generatorn till AES:s nyckel.*/
	public void init(SecureRandom sr) {
		random = sr;
	}



	/* Sparar nyckel på fil. */
	public void saveKey(Key k, String file) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(k);
			out.close();

		} catch(IOException ioe) {
			System.err.println("Problem med att skriva till: " + file);
		}
	}



	public static void main(String[] args) throws Exception {
		String file1 = args[0];
		new KeyHandler(file1);
	}
}