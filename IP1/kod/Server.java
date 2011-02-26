/*
 * @Server.java 1.0 03/08/27
 *
 */
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.Thread.*;
import java.lang.*;

 /*
  * Denna klass �r en chatserver som
  * skapar en serversocket f�rbindelse som ligger och
  * lyssnar efter om n�gon klient vill ansluta
  * sig till servern. Dessa klienter placeras
  * i en egen tr�d som l�ggs till i en datasamling n�r dessa
  * anslutit sig.
  *
  * @version 	1.0 27 Augusti 2003
  * @author 	Carina Ner�n
  */
public class Server extends JFrame {
	public Server server;
	public String clientAddress;
	public String fromClient;
	public ClientHandlers ch;
	public int counter = 0;
	public String host;
	private Socket clientSock = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private ServerSocket serverSocket;
	private JTextArea area;
	private int port;
	List list = Collections.synchronizedList(new ArrayList());

	/*
	 * Skapar en instans av Server d�r parameter �r den port
	 * som servern lyssnar p�.
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
				ch = new ClientHandlers(clientSock, serverSocket, area, this);
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



	 /*
	  * Denna metod skriver ut ett meddelande fr�n en klient
	  * till alla klienter i listan, inklusive sig sj�lv.
	  * Parametrar �r tv� str�ngar d�r den f�rsta str�ngen
	  * �r meddelandet fr�n klienten och den andra �r klientens
	  * adress.
	  */
	public synchronized void broadCast(String fc, String ca) {
		fromClient = fc;
		clientAddress = ca;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandlers) iter.next();
			ch.writeToClient(fromClient, clientAddress);
		}
	}



	 /*
	  * Denna metod tar bort en klient fr�n listan som
	  * inte l�ngre �r ansluten. Parameter �r en referens
	  * till det ClientHandler objekt som ska tas bort.
	  */
	public void removeClient(ClientHandlers clientHandler){
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandlers) iter.next();

			if (ch.equals(clientHandler)) {
				iter.remove();
				counter--;
				setTitle("SERVER ON: " + host + " PORT: " + port + " N CLIENTS: " + counter);
			}
		}
	}



	 /*
	  * Denna metod st�nger f�nstret n�r man tryckt
	  * p� krysset.
	  */
	public class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			try {
				out.close();
				in.close();
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


