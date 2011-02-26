/**
 * @ClientPiong.java 1.0 03/09/01
 *
 */

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Thread.*;

/**
  * Skapar en klient och bygger upp själva fönstret och
  * dess komponenter till klientens spelplan.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Nerén
  */
public class ClientPiong extends JFrame {
	public ClientGameCourt cCourt = new ClientGameCourt();
	public PrintWriter out = null;
	public JLabel msgLabel = new JLabel("Skriv in ditt namn!");
	public JTextArea a = new JTextArea();
	public JLabel player1 = new JLabel("Spelare 1", JLabel.CENTER);
	public JLabel infoText2 = new JLabel("", JLabel.CENTER);
	public JLabel player2 = new JLabel("Spelare 2", JLabel.CENTER);
	private BufferedReader in = null;
	private Socket s = null;
	private ClientThread ct;
	private String host;
	private int port;
	private JPanel pa1 = new JPanel();
	private JPanel msgLine = new ImagePanel("msgLine");
	private JPanel pan1 = new ImagePanel("pan1");
	private JPanel area = new ImagePanel("area");
	private JPanel buttons = new ImagePanel("buttons");
	private JPanel noButtons = new JPanel();
	private JTextField text = new JTextField(20);
	private JLabel message = new JLabel("Message: ");
	private JButton sendButton = new JButton("Send");
	private JButton b1 = new JButton("New Game");
	private JButton b2 = new JButton("Paus");
	private JButton b3 = new JButton("Continiue");
	private JButton b4 = new JButton("Exit");
	private JPanel pa2 = new JPanel();
	private JPanel pan2 = new ImagePanel("pan2");
	private JPanel pan3 = new ImagePanel("pan3");
	private JLabel infoText1 = new JLabel("-=PIONG=-", JLabel.CENTER);
	private JPanel pa3 = new JPanel();
	private JPanel pan4 = new ImagePanel("pan4");
	private JPanel pan5 = new ImagePanel("pan5");
	private JLabel point1 = new JLabel("0", JLabel.CENTER);
	private JPanel pa4 = new JPanel();
	private JPanel pan6 = new ImagePanel("pan6");
	private JPanel pan7 = new ImagePanel("pan7");
	private JLabel point2 = new JLabel("0", JLabel.CENTER);
	private String name;
	private String chatMsg;


	/**
	  * Skapar en klient som skapar en socket där klienten
	  * via denna socket ansluter sig till en angiven server
	  * adress på en angiven port. Parametrarna är just denna
	  * adress samt port.
	  *
	  * @param str adressen till den server som klienten ska ansluta sig till
	  		   x   den port som klienten ska ansluta sig till
	  */
	public ClientPiong(String str, int x) throws UnknownHostException, IOException{
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

		setTitle("PIONG");
		cCourt.setSize(350,250);
		cCourt.setBackground(Color.black);

		msgLabel.setFont(new Font("Times new Roman", Font.BOLD, 20));
		msgLabel.setForeground(Color.black);
		msgLine.setBackground(new Color(106, 106, 133));
		msgLine.setLayout(new FlowLayout());
		msgLine.add(msgLabel);
		pan1.setLayout(new FlowLayout());
		pan1.add(message);
		pan1.add(text);
		pan1.grabFocus();
		pan1.add(sendButton);
		a.setForeground(Color.black);
		a.setEditable(false);
		a.setRows(4);
		a.setColumns(32);
		JScrollPane scroll = new JScrollPane(a);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		area.setLayout(new FlowLayout());
		area.add(scroll);
		noButtons.add(msgLine); noButtons.add(pan1); noButtons.add(area);
		buttons.setLayout(new FlowLayout());
		buttons.add(b1); buttons.add(b2);
		buttons.add(b3); buttons.add(b4);
		b1.addActionListener(new Listener()); b2.addActionListener(new Listener());
		b3.addActionListener(new Listener()); b4.addActionListener(new Listener());
		sendButton.addActionListener(new Listener());
		text.addActionListener(new Listener());

		infoText1.setFont(new Font("Times new Roman", Font.BOLD, 24));
		infoText2.setFont(new Font("Times new Roman", Font.BOLD, 24));
		infoText1.setForeground(Color.black);
		infoText2.setForeground(Color.black);
		pan2.add(infoText1);
		pan3.add(infoText2);

		point1.setFont(new Font("Times new Roman", Font.BOLD, 27));
		point1.setForeground(Color.black);
		player1.setFont(new Font("Times new Roman", Font.BOLD, 20));
		player1.setForeground(Color.black);
		pan4.add(player1);
		pan5.add(point1);

		point2.setFont(new Font("Times new Roman", Font.BOLD, 27));
		point2.setForeground(Color.black);
		player2.setFont(new Font("Times new Roman", Font.BOLD, 20));
		player2.setForeground(Color.black);
		pan6.add(player2);
		pan7.add(point2);

		pa1.setLayout(new BorderLayout());
		noButtons.setLayout(new BorderLayout());
		noButtons.add(msgLine, "North");
		noButtons.add(pan1, "Center");
		noButtons.add(area, "South");
		pa1.add(noButtons, "North");
		pa1.add(buttons, "South");

		pa2.setLayout(new GridLayout(2,1));
		pa2.add(pan2);
		pa2.add(pan3);

		pa3.setLayout(new GridLayout(2,1));
		pa3.add(pan4);
		pa3.add(pan5);

		pa4.setLayout(new GridLayout(2,1));
		pa4.add(pan6);
		pa4.add(pan7);

		getContentPane().add(pa2, "North");
		getContentPane().add(cCourt, "Center");
		getContentPane().add(pa3, "West"); getContentPane().add(pa4, "East");
		getContentPane().add(pa1, "South");
		addWindowListener(new CrossListener());
		pack();
		cCourt.init(point1, point2, this);
		setVisible(true);

		ct = new ClientThread(s, this);
       	ct.activity.start();
		} else	System.exit(0);
	}



	/**
	  * Skapar en klient med den angivna adressen och
	  * default porten 2000 samt anropar konstruktorn
	  * ovan med detta.
	  *
	  * @param str adressen till den server klienten ska ansluta sig till
	  */
	ClientPiong(String str) throws UnknownHostException, IOException {
		this(str, 2000);
	}



	/**
	  * Skapar en klient med default adressen 127.0.0.1
	  * och anropar konstruktorn ovan med denna.
	  */
	ClientPiong() throws UnknownHostException, IOException {
		this("127.0.0.1");
	}



	/**
	  * Ritar upp en bild i panelen.
	  */
	 class ImagePanel extends JPanel {
		 URL url;
		 ImageIcon i;
		 Image image;

		 ImagePanel(String pan) {
			 if (pan.equals("msgLine")) {
				 url = ClassLoader.getSystemResource("bilder/msgLine.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan1")) {
				 url = ClassLoader.getSystemResource("bilder/pan1.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("area")) {
				 url = ClassLoader.getSystemResource("bilder/area.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("buttons")) {
				 url = ClassLoader.getSystemResource("bilder/buttons.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan2")) {
				 url = ClassLoader.getSystemResource("bilder/infoText1.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan3")) {
				 url = ClassLoader.getSystemResource("bilder/infoText2.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan4")) {
				 url = ClassLoader.getSystemResource("bilder/player1.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan5")) {
				 url = ClassLoader.getSystemResource("bilder/point1.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan6")) {
				 url = ClassLoader.getSystemResource("bilder/player2.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  } else if (pan.equals("pan7")) {
				 url = ClassLoader.getSystemResource("bilder/point2.JPG");
				 i = new ImageIcon(url);
				 image = i.getImage();
			  }
			 MediaTracker tracker = new MediaTracker(this);
			 tracker.addImage(image, 0);
		     try {
			    tracker.waitForID(0);
			 } catch (InterruptedException e) {}
		 }

		 public void paintComponent(Graphics g) {
			 g.drawImage(image, 0, 0, getSize().width, getSize().height, this);
	 	}
	 }




	/**
	  * Lyssnar efter knapptryckningar.
	  */
	class Listener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			cCourt.requestFocus();
			String labelText = infoText2.getText();
			String labelText2 = msgLabel.getText();
			Color co = player1.getForeground();
			if ((ae.getSource() == sendButton) || (ae.getSource() == text)) {

				if ((labelText2.equals("Spelare 1 - starta spelet!")) ||
					(labelText2.equals("Välkommen " + name))) {
					msgLabel.setText(labelText2);
				} else if (labelText2.equals("Du kan nu chatta med din motspelare!")) {
					String chatMessage = text.getText();
					text.setText("");
					String lText2 = labelText2 + "/";
					String chatMsg = lText2.concat((name + "¤" + chatMessage));
					out.println(chatMsg);
				} else {
					name = text.getText();
					text.setText("");
					msgLabel.setText("Välkommen " + name);
					out.println((labelText + "/" + name));
				}
			} else if (ae.getSource() == b1) {
				if (labelText2.equals("Spelare 1 - starta spelet!")) {
					if (co != (Color.black)) {
						out.println(("newGame" + "/" + "newGame"));
					}
				}
			} else if (ae.getSource() == b2) {
				if ((labelText2.equals("Skriv in ditt namn!")) ||
					(labelText2.equals("Välkommen " + name)) ||
					(labelText2.equals("Spelare 1 - starta spelet!"))) {
					msgLabel.setText(labelText2);
				} else {
					out.println(("pauseGame" + "/" + "pauseGame"));
				}
			} else if (ae.getSource() == b3) {
				if ((labelText2.equals("Skriv in ditt namn!")) ||
					(labelText2.equals("Välkommen " + name)) ||
					(labelText2.equals("Spelare 1 - starta spelet!"))) {
					msgLabel.setText(labelText2);
				} else {
					out.println(("continiueGame" + "/" + "continiueGame"));
				}
			} else if (ae.getSource() == b4) {
				if ((labelText2.equals("Skriv in ditt namn!")) ||
					(labelText2.equals("Välkommen " + name)) ||
					(labelText2.equals("Spelare 1 - starta spelet!"))) {
					msgLabel.setText(labelText2);
				} else {
					out.println(("exitGame" + "/" + name));
					try {
					 	s.close();
					 	out.close();
					 	in.close();
					} catch (IOException e) {
						System.out.println("IOException generated: " + e);
					}
					ct.killThread();
					dispose();
					System.exit(0);
				}
			}
		}
	}



	/**
	  * Avslutar programmet när man trycker på
	  * krysset.
	  */
	class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			out.println(("exitGame2" + "/" + name));
			ct.killThread();
			dispose();
			System.exit(0);
		}
	}



	public static void main(String[]args) throws UnknownHostException, IOException {
		if (args.length == 0) {
			new ClientPiong();
		}
		if (args.length == 1) {
			String host = args[0];
			new ClientPiong(host);
		}
		if (args.length == 2) {
			String host = args[0];
			int port = Integer.parseInt(args[1]);
			new ClientPiong(host, port);
		}
	}
}


