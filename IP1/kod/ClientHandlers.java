 /*
  * @ClientHandler.java 1.0 03/08/27
  *
  */
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.lang.*;


 /*
  * En tr�d som implementerar gr�nssnittet
  * Runnable.
  *
  * @version 	1.0 27 Augusti 2003
  * @author 	Carina Ner�n
  */
public class ClientHandlers implements Runnable {
	public Thread activity = new Thread(this);
	public Server server;
	private BufferedReader in = null;
	private boolean running = true;
	private PrintWriter out = null;
	private ServerSocket serverSocket;
	private JTextArea area;
	private String host;
	private Socket s;

	public ClientHandlers(Socket so, ServerSocket ss, JTextArea ar, Server cs) throws UnknownHostException, IOException {
		s = so;
		serverSocket = ss;
		area = ar;
		server = cs;
	}



	 /*
	  * Denna metod skriver ut textmeddelande fr�n klienten. Har
	  * tv� str�ngar som parametrar som d�r den f�rsta �r ett
	  * meddelande fr�n en klien och den andra �r denne klients
	  * adress.
	  */
	public synchronized void writeToClient(String fromClient, String clientAddress) {
		out.println(clientAddress + "- " + fromClient);
		out.flush();
	}



	 /*
	  *Denna metod s�tter running till false
	  * och talar d�rmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void killThread() {
		running = false;
	}



     /*
      * Denna metod lyssnar efter om klienten
      * skickat n�got meddelande.
      */
    public void run() {
		while(running) {
			try {
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(s.getOutputStream(), true);

				String str = s.getInetAddress().getHostName();
				area.append("CLIENT: " + str + " BROADCAST: CLIENT CONNECTED: " + str + "\n\r");

				String fromClient;
				try {
					while ((fromClient = in.readLine()) != null) {
			   			area.append("CLIENT: " + str + " BROADCAST: " + fromClient + "\n\r");
			   			server.broadCast(fromClient, str);
			   			if (fromClient.equals("Carina> exit")) {
			   				break;
						}
			   		}
			   		s.close();
			   		in.close();
			   		System.out.println("CLIENT DISCONNECTED NICELY");
			 	} catch (IOException e) {
					System.out.println("CLIENT DISCONNECTED BRUTALLY");
			 	} finally {
					if (s !=null) server.removeClient(this);
					area.append("CLIENT: " + str + " BROADCAST: CLIENT DISSCONNECTED: " + str + "\n\r");
					running = false;
				}

			} catch (IOException e) {
				System.out.println("FAILED: GET INPUTSTREAM FROM CLIENT SOCKET");
				server.removeClient(this);
				return;
			}
		}
	}
}