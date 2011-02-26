/**
  * @Client.java 1.0 03/12/12
  *
  */

import javax.swing.event.*;
import java.lang.Thread.*;
import java.awt.event.*;
import java.security.*;
import javax.crypto.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.*;

 /**
   * Denna klass är en chatklient som
   * skapar en förbindelse till en server
   * via en stream-socket. Här skapas dessutom ett
   * användargränssnitt och ett trådobjekt.
   * Klassen har tre konstruktorer som kan ta
   * emot tre variationer av argumentantal.
   * Alla meddelanden som klienten skickar är både
   * signerade och krypterade. Klienten kan även
   * skriva in ett namn som denne vill använda under
   * chatten och även detta namn signeras samt
   * krypteras tillsammans med meddelandet.
   *
   * @version 	1.0 12 December 2003
   * @author 	Carina Nerén
   */
public class Client extends JFrame {
	private ObjectOutputStream out = null;
	private BufferedInputStream in = null;
	private JButton sendMessageButton;
	private JTextField writeMessage;
	private JButton sendNameButton;
	private SecretKeyHandler skh;
	private JTextField writeName;
	private EncryptHandler eh;
	private Socket s = null;
	private String userName;
	private ClientThread ct;
	private JTextArea area;
	private SignHandler sh;
	private KeyHandler kh;
	private SecretKey sk;
	private String host;
	PublicKey pubKey;
	private int port;
	byte[] encData;

	/**
	  * Skapar en instans av klassen Client och har som parameter en ip-adress
	  * samt ett portnummer.
	  */
	Client(String str, int x) throws UnknownHostException, IOException {
		host = str;
		port = x;

		try {
			s = new Socket(host, port);
			in = new BufferedInputStream(s.getInputStream());
			out = new ObjectOutputStream(s.getOutputStream());
		} catch (UnknownHostException e) {
			System.err.println(e);
			System.exit(0);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(0);
		}

		boolean bo = s.isConnected();
		if (bo == true) {

			setTitle("CONNECTED TO: " + host + "- ON PORT: " + port);

			JPanel up = new JPanel();
			up.setBackground(new Color(128,0,128));
			writeMessage = new JTextField(20);
			writeMessage.setForeground(Color.black);
			sendMessageButton = new JButton("Skicka Meddelande");
			sendMessageButton.setFont(new Font("Times new Roman", Font.BOLD, 12));
			up.add(writeMessage);
			up.add(sendMessageButton);
			writeMessage.grabFocus();
			getContentPane().add(up, "North");

			JPanel down = new JPanel();
			down.setBackground(new Color(128,0,128));
			writeName = new JTextField(20);
			writeName.setForeground(Color.black);
			sendNameButton = new JButton("Skicka Namn");
			sendNameButton.setFont(new Font("Times new Roman", Font.BOLD, 12));
			down.add(writeName);
			down.add(sendNameButton);
			writeName.grabFocus();
			getContentPane().add(down, "South");

			JPanel east = new JPanel();
			east.setBackground(new Color(128,0,128));
			getContentPane().add(east, "East");

			JPanel west = new JPanel();
			west.setBackground(new Color(128,0,128));
			getContentPane().add(west, "West");

			area = new JTextArea();
			area.setBackground(new Color(255,223,255));
			area.setForeground(Color.red);
			area.setEditable(false);
			JScrollPane scroll = new JScrollPane(area);
			getContentPane().add(scroll, "Center");
			area.append("MEDDELANDE FRÅN SERVERN: " + "\n\r" + "Skriv in det användarnamn du vill ha under chatten i textfältet "
													+ "\n\r" + "nedan och tryck därefter på Skicka Namn - knappen!" + "\n\r" + "\n\r");

			addWindowListener(new CrossListener());
			Listener l = new Listener();
			writeMessage.addActionListener(l);
			sendMessageButton.addActionListener(l);
			writeName.addActionListener(l);
			sendNameButton.addActionListener(l);

			setLocation(400,100);
			setSize(400,500);
			show();

			ct = new ClientThread(s, area);
       		ct.activity.start();
		} else	System.exit(0);
	}



	/**
	  * Skapar en instans av Client som har en ip-adress som portnummer.
	  */
	Client(String str) throws UnknownHostException, IOException {
		this(str, 2000);
	}



	Client() throws UnknownHostException, IOException {
		this("127.0.0.1");
	}



	/**
	  * En lyssnare som stänger fönstret,när man
	  * trycker på krysset.
	  */
	public class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			ct.killThread();
			dispose();
			System.exit(0);
		}
	}



	/**
	  * Lyssnare som lyssnar efter om något skrivits in
	  * i något av textfältet, om så gjorts läses texten in.
	  * Om Skicka Meddelandeknappen eller enter tryckts
	  * genereras ett nyckelpar där den privata nyckeln
	  * signerar texten. Därefter genereras en hemlig nyckel
	  * som krypterar den signerade texten. Till sist skickas
	  * texten, den hemliga nyckeln, den krypterad
	  * texten samt den publika nyckeln till servern.
	  * Om Skicka Namnknappen eller enter tryckts hämtas
	  * det namn som klienten vill använda under chatten.
	  * Detta namn skickas signeras även tillsammans med
	  * meddelandet.
	  */
	public class Listener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if ((ae.getSource() == writeMessage) || (ae.getSource() == sendMessageButton)) {
				String fromUser = writeMessage.getText();
				writeMessage.setText("");

				kh = new KeyHandler();
				pubKey = kh.getPublicKey();
				PrivateKey prKey = kh.getPrivateKey();

				sh = new SignHandler((userName + "/" + fromUser), prKey);
				byte msgSignature[] = sh.getMessageSignature();

				skh = new SecretKeyHandler();
				sk = skh.getSecretKey();
				Key k = (Key) sk;
				eh = new EncryptHandler(msgSignature, sk);
				encData = eh.getEncryptedData();

				try {
					out.writeObject(userName + "/" + fromUser);
					out.writeObject(sk);
					out.writeObject(encData);
					out.writeObject(pubKey);
				} catch (IOException io) {
					io.printStackTrace();
				}
				try {
					if (fromUser.equals("exit")){
						out.close();
						in.close();
						s.close();
						ct.killThread();
						dispose();
						System.exit(0);
					}
				} catch (IOException e) {
				System.out.println("IOException generated: " + e);
				e.printStackTrace();
				}
			} else if ((ae.getSource() == writeName) || (ae.getSource() == sendNameButton)) {
				area.setForeground(new Color(128,0,128));
				userName = writeName.getText();
				writeName.setText("");

				kh = new KeyHandler();
				pubKey = kh.getPublicKey();
				PrivateKey prKey = kh.getPrivateKey();

				sh = new SignHandler(("MEDDELANDE FRÅN SERVERN" + "/" + ("\n\r" + userName + " har anslutit sig till chatten!")), prKey);
				byte msgSignature[] = sh.getMessageSignature();

				skh = new SecretKeyHandler();
				sk = skh.getSecretKey();
				Key k = (Key) sk;
				eh = new EncryptHandler(msgSignature, sk);
				encData = eh.getEncryptedData();

				try {
					out.writeObject("MEDDELANDE FRÅN SERVERN" + "/" + ("\n\r" + userName + " har anslutit sig till chatten!"));
					out.writeObject(sk);
					out.writeObject(encData);
					out.writeObject(pubKey);
				} catch (IOException io) {
					io.printStackTrace();
				}
				//area.append(userName + " har anslutit sig till chatten!");
			}
		}
	}



	public static void main(String[]args) throws UnknownHostException, IOException {
		if (args.length == 0) {
			new Client();
		}
		if (args.length == 1) {
			String host = args[0];
			new Client(host);
		}
		if (args.length == 2) {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			new Client(host, port);
		}
	}
}


