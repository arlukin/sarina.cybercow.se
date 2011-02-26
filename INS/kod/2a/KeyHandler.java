/*
 * @KeyHandler.java 1.0 03/12/02
 *
 */

import java.security.*;
import java.io.*;

/*
 * Generar ett nyckelpar med en publik samt en privat nyckel.
 * Respektive nyckel sparas på en godtycklig fil.
 *
 * @version 	1.0 2 December 2003
 * @author 	Carina Nerén
 */
public class KeyHandler extends KeyPairGenerator {
	private SecureRandom random;
	private PrivateKey privKey;
	private PublicKey pubKey;
	private String file1;
	private String file2;

	/*
	 * Skapar en instans av klassen KeyHandler och har som parametrar
	 * en referens till den fil som den privata nyckeln ska sparas på
	 * samt till den fil som den publika nyckeln ska sparas på.
	 */
	KeyHandler(String f1, String f2) {
		super("DSA");
		file1 = f1;
		file2 = f2;

		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
			System.out.println("DONE MAKING KEY GENERATOR");
			kpg.initialize(512, new SecureRandom());
			System.out.println("DONE MAKING KEY GENERATOR INITIALISATION");
			KeyPair kp = kpg.generateKeyPair();
			System.out.println("DONE MAKING KEYPAIR");
			pubKey = kp.getPublic();
			privKey = kp.getPrivate();
		} catch(NoSuchAlgorithmException e) {
		}
		saveKey(privKey, file1);
		System.out.println("DONE SAVING PRIVATEKEY TO FILE " + file1);
		saveKey(pubKey, file2);
		System.out.println("DONE SAVING PUBLICKEY TO FILE " + file2);
	}



	/* Initialiserar generatorn till DSA:s nyckelpar. */
	public void initialize(int strength, SecureRandom sr) {
		random = sr;
	}



	/* Sparar respektive nyckel på en fil. */
	public void saveKey(Key k, String file) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(file));
			out.writeObject(k);
			out.close();

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}



	public static void main(String[] args) throws Exception {
		String file1 = args[0];
		String file2 = args[1];
		new KeyHandler(file1, file2);

	}
}