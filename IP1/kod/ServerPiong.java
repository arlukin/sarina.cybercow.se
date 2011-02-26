/**
 * @ServerPiong.java 1.0 03/09/01
 *
 */

import javax.swing.event.*;
import java.lang.Thread.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
  * Skapar en server som ger en socket som lyssnar på
  * den specificierade porten och när en klient
  * vill ansluta sig returneras den socket som
  * skapats av klienten. En lista som håller alla
  * klienter skapas också samt ett gränssnitt.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Nerén
  */
public class ServerPiong extends JFrame {
	public ServerGameCourt sCourt = new ServerGameCourt();
	public ServerPiong serverPiong;
	public String clientAddress;
	public String clientName1;
	public String clientName2;
	public String fromClient;
	public ClientHandler ch;
	public int counter = 0;
	public ServerGameGUI sGG;
	public String host;
	private Socket piongSock = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private ServerSocket server;
	private JTextArea area;
	private String msg;
	private int port;

	List list = Collections.synchronizedList(new ArrayList());

	/**
	  * Skapar en server som skapar en ServerSocket där servern
	  * via denna socket ligger och lyssnar efter om någon klient
	  * vill koppla upp sig. Har en port som parameter.
	  *
	  * @param x den port som servern lyssnar på
	  */
	public ServerPiong(int x) throws UnknownHostException, IOException {
		port = x;

		try {
			server = new ServerSocket(port);
			host = server.getInetAddress().getLocalHost().getHostName();
			setTitle("SERVER ON: " + host + " PORT: " + port + " N CLIENTS: " + counter);
			area = new JTextArea();
			area.setEditable(false);
			JScrollPane scroll = new JScrollPane(area);
			getContentPane().add(scroll, "Center");
			setSize(500,300);
			show();

			while(true) {
				piongSock = server.accept();
				ch = new ClientHandler(piongSock, server, area, this);
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



	/**
	  * Skapar en server där porten sätts till
	  * defaultvärdet 2000.
	  */
	public ServerPiong() throws UnknownHostException, IOException {
		this(2000);
	}



	/**
	  * Skapar ett nytt objekt av ServerGameGUI.
	  */
	public void newServerGameGUI() {
		sGG = new ServerGameGUI(this);

	}



	/**
	  * Skriver ut ett meddelande till samma klient som skickat
	  * in meddelandet (sitt namn) till servern. Detta meddelande talar om
	  * ifall denna klient blir spelare 1 eller spelare 2. Parametrar är två
	  * strängar samt en referens till objektet ClientHandler.
	  *
	  * @param msg           en "kod" som indikerar vad som ska göras
	  		   name          innehåller den information som ska användas
	  		   clientHandler en referens till objektet ClientHandler vilken ska anropas
	  */
	public synchronized void cast(String msg, String name, ClientHandler clientHandler) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			if (ch.equals(clientHandler)) {
				if (msg.equals("Spelare1" + "/")) {
					clientName1 = name + " ";
					ch.writeToClient(msg, name);
				} else if (msg.equals("Spelare2" + "/")) {
					clientName2 = name;
					ch.writeToClient(msg, clientName2);
				}
			}
		}
	}



	/**
	  * Denna metod skriver ut ett meddelande från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  * Parametrar är två strängar.
	  *
	  * @param msg  en "kod" till som indikerar vad som ska göras
	  		   name innehåller den information som ska användas
	  */
	public synchronized void broadCast(String msg, String name) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ch = (ClientHandler) iter.next();
			if (msg.equals("Nytt namn2" + "/")) {
				String players = clientName1.concat(clientName2);
				ch.writeToClient(msg, players);
			} else if (msg.equals("winnerPlayer1" + "/")) {
				ch.writeToClient(msg, clientName1);
			} else if (msg.equals("winnerPlayer2" + "/")) {
				ch.writeToClient(msg, clientName2);
			} else ch.writeToClient(msg, name);
		}
	}



	/**
	  * Denna metod tar bort en klient från listan som
	  * inte längre är ansluten. Parameter är en referens
	  * till objektet ClientHandler.
	  *
	  * @param referens till objektet ClientHandler vilken är den som ska tas bort ur listan
	  */
	public synchronized void removeClient(ClientHandler clientHandler) {
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
	  * på krysset. Alla socketar, strömmar och trådar
	  * avslutas.
	  */
	public class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			try {
				out.close();
				in.close();
				piongSock.close();
				server.close();
				ch.killThread();
				sCourt.stopIt();
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
			new ServerPiong();
		}
		if (args.length == 1) {
			int port = Integer.parseInt(args[1]);
			new ServerPiong(port);
		}
	}
}


