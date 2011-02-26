/**
  * @Server.java 1.0 03/12/12
  *
  */

import javax.swing.event.*;
import java.lang.Thread.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
  * Denna klass är en chatserver som
  * skapar en serversocket förbindelse som ligger och
  * lyssnar efter om någon klient vill ansluta
  * sig till servern. Dessa klienter placeras
  * i en egen tråd som läggs till i en datasamling när dessa
  * anslutit sig.
  *
  * @version 	1.0 12 December 2003
  * @author 	Carina Nerén
  */
public class Server extends JFrame {
	public String signature;
	public ClientHandler ch;
	public int counter = 0;
	public Server server;
	public String host;
	private BufferedInputStream in = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream ois = null;
	private ServerSocket serverSocket;
	private Socket clientSock = null;
	private PublicKey pubKey;
	private SecretKey secKey;
	private byte[] encData;
	private JTextArea area;
	private byte[] sign;
	private String msg;
	private int port;

	List list = Collections.synchronizedList(new ArrayList());

	/**
	  * Skapar en instans av klassen Server där parameter är den port
	  * som servern lyssnar på.
	  */
	public Server(int x) throws UnknownHostException, IOException {
		port = x;

		try {
			serverSocket = new ServerSocket(port);
			host = serverSocket.getInetAddress().getLocalHost().getHostName();
			setTitle("SERVER ON: " + host + " PORT: " + port + " N CLIENTS: " + counter);
			area = new JTextArea();
			area.setEditable(false);
			JScrollPane scroll = new JScrollPane(area);
			getContentPane().add(scroll, "Center");
			setSize(500,300);
			show();

			while(true) {
				clientSock = serverSocket.accept();
				ch = new ClientHandler(clientSock, serverSocket, area, this);
				list.add(ch);
				ch.activity.start();
				counter++;
				setTitle("SERVER ON: " + host + " PORT: " + port + " N CLIENTS: " + counter);
			}
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
		addWindowListener(new CrossListener());
	}



	public Server() throws UnknownHostException, IOException {
		this(2000);
	}



	/**
	  * Denna metod skriver ut ett meddelande från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  */
	public synchronized void broadCastMessage(String m) {
		msg = m;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			ch.writeToClient(msg);
		}
	}



	/**
	  * Denna metod skriver ut en publik nyckel från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  */
	public synchronized void broadCastKey(PublicKey pk) {
		pubKey = pk;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			ch.writeKeyToClient(pubKey);
		}
	}



	/**
	  * Denna metod skriver ut en hemlig nyckel från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  */
	public synchronized void broadCastSecretKey(SecretKey sk) {
		secKey = sk;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			ch.writeSecretKeyToClient(secKey);
		}
	}



	/**
	  * Denna metod skriver ut den krypterade datan från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  */
	public synchronized void broadCastEncData(byte[] ed) {
		encData = ed;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			ch.writeEncDataToClient(encData);
		}
	}



	/**
	  * Denna metod tar bort en klient från listan som
	  * inte längre är ansluten. Parameter är en referens
	  * till det ClientHandler objekt som ska tas bort.
	  */
	public void removeClient(ClientHandler clientHandler){
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();

			if (ch.equals(clientHandler)) {
				iter.remove();
				counter--;
				setTitle("SERVER ON: " + host + " PORT: " + port + " N CLIENTS: " + counter);
			}
		}
	}



	/**
	  * Denna metod stänger fönstret när man tryckt
	  * på krysset.
	  */
	public class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			try {
				out.close();
				in.close();
				ois.close();
				clientSock.close();
				serverSocket.close();
				ch.killThread();
				dispose();
				System.exit(0);
			} catch (IOException e) {
				System.out.println("IOException generated: " + e);
				System.exit(0);
			}
		}
	}



	public static void main(String[]args) throws UnknownHostException, IOException {
		if (args.length == 0) {
			new Server();
		}
		if (args.length == 1) {
			int port = Integer.parseInt(args[1]);
			new Server(port);
		}
	}
}


