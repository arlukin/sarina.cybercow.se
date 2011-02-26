/**
 * @ClientHandler.java 1.0 03/09/01
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

/**
  * Skapar en tråd som tar hand om en klient
  * och som får in klientens socket, serverns
  * socket, serverns textarea samt ett objekt av
  * servern själv.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Nerén
  */
public class ClientHandler implements Runnable {
	public Thread activity = new Thread(this);
	public ServerPiong serverPiong;
	private BufferedReader in = null;
	private boolean running = true;
	private PrintWriter out = null;
	private ServerSocket server;
	private JTextArea area;
	private String host;
	private Socket s;

	public ClientHandler(Socket so, ServerSocket ss, JTextArea ar, ServerPiong sp) throws UnknownHostException, IOException {
		s = so;
		server = ss;
		area = ar;
		serverPiong = sp;
	}



	/**
	  * Skickar meddelande till klienten. Parametrar är två strängar.
	  *
	  * @param msg        en "kod" till mottagaren som indikerar vad som ska göras
	  		   fromClient innehåller information som ska användas av mottagaren
	  */
	public synchronized void writeToClient(String msg, String fromClient) {
		String string = msg.concat(fromClient);
		out.println(string);
		out.flush();
	}



	/**
	  *Denna metod sätter running till false
	  * och talar därmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void killThread() {
		running = false;
	}



    /**
      * Lyssnar efter meddelanden från
      * klienten. Skapar en in och utström
      * varifrån meddelanden kan tas emot och
      * skickas. Meddelanden kommer in som en
      * sträng som består av två ihopslagna strängar
      * där den första strängen består av ett textmeddelande
      * som indikerar vad som ska göras och den andra
      * strängen innehåller informationen som servern
      * ska göra något med.
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
						String [] strings = fromClient.split("/");
						String msg = strings[0];
						String toDo = strings[1];

						if (msg.equals("")) {
							serverPiong.cast(("Spelare1" + "/"), toDo, this);
							serverPiong.broadCast(("Nytt namn" + "/"), toDo);
						} else if (msg.equals("Spelare 1 är inskriven - vänta på spelare 2!")) {
							serverPiong.cast(("Spelare2" + "/"), toDo, this);
							serverPiong.broadCast(("Nytt namn2" + "/"), toDo);
						} else if (msg.equals("Start")) {
							serverPiong.broadCast(msg + "/", toDo);
						} else if (msg.equals("newGame")) {
							serverPiong.newServerGameGUI();
							serverPiong.broadCast(("Chatta" + "/"), "Chatta");
						} else if (msg.equals("pauseGame")) {
							serverPiong.sCourt.pauseGame();
						} else if (msg.equals("continiueGame")) {
							serverPiong.sCourt.startGame();
						} else if (msg.equals("exitGame")) {
							serverPiong.sCourt.exitGame();
							serverPiong.broadCast(("exitPlayer" + "/"), toDo);
							break;
						} else if (msg.equals("exitGame2")) {
							serverPiong.sCourt.exitGame();
							serverPiong.broadCast(("exitPlayer" + "/"), toDo);
						} else if (msg.equals("le_up")) {
							serverPiong.sCourt.setLeRUp();
							serverPiong.broadCast((msg + "/"), toDo);
						} else if (msg.equals("le_down")) {
							serverPiong.sCourt.setLeRDown();
							serverPiong.broadCast((msg + "/"), toDo);
						} else if (msg.equals("ri_up")) {
							serverPiong.sCourt.setRiRUp();
							serverPiong.broadCast((msg + "/"), toDo);
						} else if (msg.equals("ri_down")) {
							serverPiong.sCourt.setRiRDown();
							serverPiong.broadCast((msg + "/"), toDo);
						} else if (msg.equals("Du kan nu chatta med din motspelare!")) {
							serverPiong.broadCast(("Chattmeddelande" + "/"), toDo);
						}
					}
			   		in.close();
			   		s.close();
			   		System.out.println("CLIENT DISCONNECTED NICELY");
			 	} catch (IOException e) {
					System.out.println("CLIENT DISCONNECTED BRUTALLY");
			 	} finally {
					if (s !=null) serverPiong.removeClient(this);
					area.append("CLIENT: " + str + " BROADCAST: CLIENT DISSCONNECTED: " + str + "\n\r");
					running = false;
				}
			} catch (IOException e) {
				System.out.println("FAILED: GET INPUTSTREAM FROM CLIENT SOCKET");
				serverPiong.removeClient(this);
				return;
			}
		}
	}
}