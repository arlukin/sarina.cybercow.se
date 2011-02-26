/**
  * @ClientHandler.java 1.0 03/12/12
  *
  */

import javax.swing.event.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.*;
import java.util.*;
import java.lang.*;
import java.awt.*;
import java.net.*;
import java.io.*;

/**
  * En tråd som implementerar gränssnittet
  * Runnable.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class ClientHandler implements Runnable {
	public Thread activity = new Thread(this);
	public Server server;
	private ObjectOutputStream out = null;
	private ObjectInputStream ois = null;
	private ServerSocket serverSocket;
	private BufferedReader in = null;
	private BufferedInputStream bis;
	private boolean running2 = true;
	private boolean running = true;
	private byte encData[];
	private JTextArea area;
	private SecretKey sk;
	private String host;
	private String str;
	private Socket s;

	/**
	  * Skapar en instans av klassen ClientHandler och har som parametrar en
	  * referens till klientens socket, serverns socket, serverns
	  * textarea samt till servern.
	  */
	public ClientHandler(Socket so, ServerSocket ss, JTextArea ar, Server cs) throws UnknownHostException, IOException {
		s = so;
		serverSocket = ss;
		area = ar;
		server = cs;
	}



	/**
	  * Skickar ett meddelande till klienten.
	  */
	public synchronized void writeToClient(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}



	/**
	  * Skickar en publik nyckel till klienten.
	  */
	public synchronized void writeKeyToClient(PublicKey pk) {
		try {
			out.writeObject(pk);
			out.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}



	/**
	  * Skickar en hemlig nyckel till klienten.
	  */
	public synchronized void writeSecretKeyToClient(SecretKey sk) {
		try {
			out.writeObject(sk);
			out.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}



	/**
	  * Skickar den krypterade datan till klienten.
	  */
	public synchronized void writeEncDataToClient(byte[] ed) {
		try {
			out.writeObject(ed);
			out.flush();
		} catch (IOException io) {
			io.printStackTrace();
		}
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
      * Denna metod lyssnar efter om klienten
      * skickat något meddelande.
      */
    public void run() {
		while(running) {
			try {
				String fromClient = null;

				BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
				ois = new ObjectInputStream(bis);
				out = new ObjectOutputStream(s.getOutputStream());

				str = s.getInetAddress().getHostName();
				area.append("CLIENT: " + str + " BROADCAST: CLIENT CONNECTED: " + str + "\n\r");
				Object o;

				try {
					while((o = ois.readObject()) != null) {
						try {
							fromClient = (String) o;
							if (fromClient != null) {
								server.broadCastMessage(fromClient);
								if (fromClient.equals("exit")) {
									s.close();
									ois.close();
									System.out.println("CLIENT DISCONNECTED NICELY");
									break;
								}
							}
						} catch (ClassCastException cce) {
							System.out.println("Unexpected data in file1");
							System.exit(-1);
						}
						o = ois.readObject();

						try {
							sk = (SecretKey) o;
							if (sk != null) {
								server.broadCastSecretKey(sk);
							}
						} catch (ClassCastException cce) {
							System.out.println("Unexpected data in file2");
							System.exit(-1);
						}
						o = ois.readObject();

						try {
							encData = (byte []) o;
							if (encData != null) {
								server.broadCastEncData(encData);
							}
						} catch (ClassCastException cce) {
							System.out.println("Unexpected data in file3");
							System.exit(-1);
						}
						o = ois.readObject();

						try {
							PublicKey pubKey = (PublicKey) o;
							if (pubKey != null) {
								server.broadCastKey(pubKey);
							}
						} catch (ClassCastException cce) {
							System.out.println("Unexpected data in file4");
							System.exit(-1);
						}
					}
				} catch (IOException e) {
					System.out.println("CLIENT DISCONNECTED BRUTALLY");
					running2 = false;
				} finally {
					if (s !=null) server.removeClient(this);
					area.append("CLIENT: " + str + " BROADCAST: CLIENT DISSCONNECTED: " + str + "\n\r");
					running = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("FAILED: GET INPUTSTREAM FROM CLIENT SOCKET");
				server.removeClient(this);
				return;
			} catch (ClassNotFoundException cnfe) {
				cnfe.printStackTrace();
			}
		}
	}
}