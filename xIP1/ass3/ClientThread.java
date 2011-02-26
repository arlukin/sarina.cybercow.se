 /*
  * @ClientThread.java 1.0 03/08/27
  *
  */

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;



 /*
  * En tr�d som implementerar gr�nssnittet
  * Runnable.
  *
  * @version 	1.0 27 Augusti 2003
  * @author 	Carina Ner�n
  */
public class ClientThread implements Runnable {
	public Thread activity = new Thread(this);
	private Socket s;
	private JTextArea area;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean running = true;

	/*
	 * Skapar en instans av ClientThread d�r parametrarna �r
	 * en klient socket samt en textarea.
	 */
	public ClientThread(Socket so, JTextArea a) throws UnknownHostException, IOException {
		s = so;
		area = a;
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
      * Denna metod ligger och lyssnar efter ett
      * meddelande fr�n servern och l�ser in
      * det samt skriver ut detta i textarean.
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
					area.append("CLIENT: " + fromServer + "\n\r");
				}
				s.close();
				in.close();
			} catch (IOException e) {
				System.err.println(e);
				System.exit(0);
			}
		}
	}
}