/*
  * @Client.java 1.0 03/08/27
  *
  */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.Thread.*;

 /*
  * Denna klass är en chatklient som
  * skapar en förbindelse till en server
  * via en stream-socket. Här skapas dessutom ett
  * användargränssnitt och ett trådobjekt.
  * Klassen har tre konstruktorer som kan ta
  * emot tre variationer av argumentantal.
  *
  * @version 	1.0 29 Juli 2003
  * @author 	Carina Nerén
  */
public class Client extends JFrame {
	private String host;
	private int port;
	private JTextField write;
	private JTextArea area;
	private Socket s = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private ClientThread ct;
	private KeyHandler kh;
	private SignHandler sh;

	/*
	 * Skapar en instans av Client och har som parameter en ip-adress
	 * samt ett portnummer.
	 */
	Client(String str, int x) throws UnknownHostException, IOException {
		host = str;
		port = x;

		try {
			s = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(s.getOutputStream(), true);
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
			up.setBackground(new Color(155,0,155));
			write = new JTextField(20);
			write.setBackground(new Color(255,175,255));
			write.setForeground(Color.black);
			up.add(write);
			write.grabFocus();
			getContentPane().add(up, "North");

			area = new JTextArea();
			area.setBackground(new Color(255,175,255));
			area.setForeground(new Color(155,0,155));
			area.setEditable(false);
			JScrollPane scroll = new JScrollPane(area);
			getContentPane().add(scroll, "Center");

			addWindowListener(new CrossListener());
			Listener l = new Listener();
			write.addActionListener(l);

			setLocation(400,300);
			setSize(400,500);
			show();

			ct = new ClientThread(s, area);
       		ct.activity.start();
		} else	System.exit(0);
	}



	/*
	 * Skapar en instans av Client som har en ip-adress som portnummer.
	 */
	Client(String str) throws UnknownHostException, IOException {
		this(str, 2000);
	}



	Client() throws UnknownHostException, IOException {
		this("127.0.0.1");
	}



	 /*
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



	 /*
	  *Lyssnare som lyssnar efter om något skrivits in
	  * i textfältet. Om så gjorts läses texten in och
	  * skickas till servern.
	  */
	public class Listener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == write) {
				String fromUser = write.getText();
				write.setText("");
				out.println("Carina> " + fromUser);
				out.flush();
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
				}
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


