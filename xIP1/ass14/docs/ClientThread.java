/**
 * @ClientThread.java 1.0 03/09/01
 *
 */

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

/**
  * Skapar en tråd som lyssnar efter
  * meddelanden från servern. Tar in
  * klientens socket samt ett objekt av
  * klienten.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Nerén
  */
public class ClientThread implements Runnable {
	public Thread activity = new Thread(this);
	private Socket s;
	private ClientPiong clientPiong;
	private ClientGameCourt cCourt;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean running = true;

	public ClientThread(Socket so, ClientPiong cp) throws UnknownHostException, IOException {
		s = so;
		clientPiong = cp;
	}



	/**
	  * Sätter running till false
	  * och talar därmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void killThread() {
		running = false;
	}



    /**
      * Lyssnar efter meddelande från servern. Skapar in och ut
      * strömmar via meddelanden kan tas emot och skickas.
      * Meddelanden kommer in som en sträng som består av två
      * ihopslagna strängar där den första strängen består av
      * ett textmeddelande som indikerar vad som ska göras och
      * den andra strängen innehåller informationen som klienten
      * ska göra något med.
      */
    public void run() {
		while(running){
			try {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
					System.err.println(ie);
				}
				in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				out = new PrintWriter(s.getOutputStream(), true);

				String fromServer;

				while ((fromServer = in.readLine()) != null) {
					String [] strings = fromServer.split("/");
					String msg = strings[0];
					String toDo = strings[1];

					if (msg.equals("Spelare1")) {
						clientPiong.player1.setForeground(new Color(163, 24, 30));
					} else if (msg.equals("Nytt namn")) {
						clientPiong.infoText2.setText("Spelare 1 är inskriven - vänta på spelare 2!");
					} else if (msg.equals("Spelare2")) {
						clientPiong.player2.setForeground(new Color(163, 24, 30));
					} else if (msg.equals("Nytt namn2")) {
						String [] strings2 = toDo.split(" ");
						String str1 = strings2[0];
						String str2 = strings2[1];
						clientPiong.infoText2.setText(str1 + " VS. " + str2);

						String str3 = "Start" + "/";
						String str4 = "inget";
						String str5 = str3.concat(str4);
						clientPiong.out.println(str5);
					} else if (msg.equals("Start")) {
						clientPiong.msgLabel.setText("Spelare 1 - starta spelet!");
					} else if (msg.equals("position")) {
						String [] strings1 = toDo.split(" ");
						int x = Integer.parseInt(strings1[0]);
						int y = Integer.parseInt(strings1[1]);
						clientPiong.cCourt.setPosition(x, y);
					} else if (msg.equals("point1")) {
						clientPiong.cCourt.setPoint1(toDo);
					} else if (msg.equals("point2")) {
						clientPiong.cCourt.setPoint2(toDo);
					} else if (msg.equals("pling")) {
						Toolkit.getDefaultToolkit().beep();
					} else if (msg.equals("le_up")) {
						clientPiong.cCourt.setLeRUp();
					} else if (msg.equals("le_down")) {
						clientPiong.cCourt.setLeRDown();
					} else if (msg.equals("ri_up")) {
						clientPiong.cCourt.setRiRUp();
					} else if (msg.equals("ri_down")) {
						clientPiong.cCourt.setRiRDown();
					} else if (msg.equals("Chatta")) {
						clientPiong.msgLabel.setText("Du kan nu chatta med din motspelare!");
					} else if (msg.equals("Chattmeddelande")) {
						String [] strings3 = toDo.split("¤");
						String s1 = strings3[0];
						String s2 = strings3[1];
						clientPiong.a.append(s1 + ": " + s2 + "\n\r");
					} else if (msg.equals("exitPlayer")) {
						clientPiong.msgLabel.setText("Spelare " + toDo + " har lämnat spelet!");
						clientPiong.infoText2.setText("Spelet är slut!");
					} else if (msg.equals("winnerPlayer1")) {
						clientPiong.infoText2.setText("Vinnare är " + toDo + " !");
						clientPiong.msgLabel.setText("Spelet är slut!");
					} else if (msg.equals("winnerPlayer2")) {
						clientPiong.infoText2.setText("Vinnare är " + toDo + " !");
						clientPiong.msgLabel.setText("Spelet är slut!");
					}
				}
			} catch (IOException e) {
				System.err.println(e);
				System.exit(0);
			}
		}
	}
}