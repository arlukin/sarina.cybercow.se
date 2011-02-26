/**
  * @ClientThread.java 1.0 03/12/12
  *
  */

import javax.swing.event.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.*;

/**
  * En tråd som implementerar gränssnittet
  * Runnable.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class ClientThread implements Runnable {
	public Thread activity = new Thread(this);
	public String clientName;
	private ObjectInputStream ois = null;
	private boolean running = true;
	private DecryptHandler dh;
	private String signString;
	private String fromServer;
	private PublicKey pubKey;
	private SecretKey secKey;
	private SecretKey secKey2;
	private byte[] signature;
	private VerifyHandler vh;
	private JTextArea area;
	private byte[] encData;
	private String message;
	private Socket s;

	/**
	  * Skapar en instans av klassen ClientThread där parametrarna är en
	  * referens till en klientens socket samt till klientens textarea.
	  */
	public ClientThread(Socket so, JTextArea a) throws UnknownHostException, IOException {
		s = so;
		area = a;
	}



	/**
	  * Denna metod sätter running till false
	  * och talar därmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void killThread() {
		running = false;
	}



    /**
      * Denna metod ligger och lyssnar efter meddelanden
      * från servern. Dessa läses in och därefter avkrypteras
      * först meddelandet med en hemlig nyckel, sedan
      * kontrolleras om den publika nyckeln och det avkrypterade
      * meddelandet verifierar texten. Om texten är valid skrivs den
      * därefter ut i textarean.
      */
    public void run() {
		while(running){
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					System.err.println(ie);
				}
				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
				ois = new ObjectInputStream(bis);
				Object o;

				while((o = ois.readObject()) != null) {
					try {
						fromServer = (String) o;

						String [] strings = fromServer.split("/");
						clientName = strings[0];
						message = strings[1];
					} catch (ClassCastException cce) {
						System.out.println("Unexpected data in file5");
						System.exit(-1);
					}
					o = ois.readObject();

					try {
						secKey = (SecretKey) o;
					} catch (ClassCastException cce) {
						System.out.println("Unexpected data in file6");
						System.exit(-1);
					}
					o = ois.readObject();

					try {
						encData = (byte []) o;
					} catch (ClassCastException cce) {
						System.out.println("Unexpected data in file7");
						System.exit(-1);
					}
					o = ois.readObject();

					try {
						pubKey = (PublicKey) o;
					} catch (ClassCastException cce) {
						System.out.println("Unexpected data in file8");
						System.exit(-1);
					}
					dh = new DecryptHandler(encData, secKey);
					signature = dh.getDecryptedData();
					vh = new VerifyHandler(fromServer, pubKey, signature);
					String msgString = vh.getMessageString();
					if (msgString.equals("MESSAGE VALID")) {
						System.out.println("MESSAGE VALID");
						area.append(clientName + ": " + message + "\n\r");
					} else if (msgString.equals("MESSAGE WAS CORRUPTED")) {
						System.out.println("MESSAGE WAS CORRUPTED");
						area.append("Message was corrupted!");
					}
				}
				s.close();
				ois.close();
			} catch (IOException e) {
				System.err.println(e);
				System.exit(0);
				e.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
	}
}