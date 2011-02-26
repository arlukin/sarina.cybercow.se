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
  * Denna klass är en chatserver som
  * skapar en serversocket förbindelse som ligger och
  * lyssnar efter om någon klient vill ansluta
  * sig till servern. Dessa klienter placeras
  * i en egen tråd som läggs till i en datasamling när dessa
  * anslutit sig.
  *
  * @version 	1.0 27 Augusti 2003
  * @author 	Carina Nerén
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
	 * Skapar en instans av Server där parameter är den port
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
	  * Denna metod skriver ut ett meddelande från en klient
	  * till alla klienter i listan, inklusive sig själv.
	  * Parametrar är två strängar där den första strängen
	  * är meddelandet från klienten och den andra är klientens
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
	  * Denna metod tar bort en klient från listan som
	  * inte längre är ansluten. Parameter är en referens
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
	  * Denna metod stänger fönstret när man tryckt
	  * på krysset.
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


