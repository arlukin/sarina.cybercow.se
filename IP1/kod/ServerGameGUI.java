/**
 * @ServerGameGUI.java 1.0 03/09/01
 *
 */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

/**
  * Bygger upp själva fönstret till serverns
  * spelplan med dess komponenter.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Nerén
  */
public class ServerGameGUI extends JFrame {
	public ServerPiong server;
	private JPanel label1 = new JPanel();
	private JPanel label2 = new JPanel();
	private JLabel point1 = new JLabel("0", JLabel.CENTER);
	private JLabel point2 = new JLabel("0", JLabel.CENTER);
	private JPanel panel = new JPanel();
	private JLabel label = new JLabel("PIONG", JLabel.CENTER);
	private JPanel pa = new JPanel();
	private JPanel pa1 = new JPanel();
	private JPanel buttons = new JPanel();
	private JTextField text = new JTextField(20);
	private JLabel message = new JLabel("Message: ");
	private JButton sendButton = new JButton("Send");
	private JButton b1 = new JButton("New Game");
	private JButton b2 = new JButton("Paus");
	private JButton b3 = new JButton("Continiue");
	private JButton b4 = new JButton("Exit");

	/**
	  * Bygger ett fönster och har en referens till objektet ServerPiong
	  * som parameter.
	  *
	  * @param s en referens till objektet ServerPiong
	  */
	public ServerGameGUI(ServerPiong s) {
		server = s;

		setTitle("PIONG");
		server.sCourt.setSize(350,250);
		server.sCourt.setBackground(Color.black);
		point1.setFont(new Font("Times new Roman", Font.BOLD, 24));
		point2.setFont(new Font("Times new Roman", Font.BOLD, 24));
		label1.setBackground(Color.gray);
		label2.setBackground(Color.gray);
		label1.add(point1); label2.add(point2);

		pa1.add(message);
		pa1.add(text);
		pa1.grabFocus();
		pa1.add(sendButton);
		pa1.setBackground(Color.gray);
		pa1.setLayout(new FlowLayout());
		buttons.setBackground(Color.gray);
		buttons.setLayout(new FlowLayout());
		buttons.add(b1); buttons.add(b2);
		buttons.add(b3); buttons.add(b4);

		pa.setLayout(new GridLayout(2,1));
		pa.add(pa1);

		pa.add(buttons);
		panel.setBackground(Color.gray);
		label.setFont(new Font("Times new Roman", Font.BOLD, 24));
		panel.add(label);
		addWindowListener(new CrossListener());

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, "North");
		getContentPane().add(server.sCourt, "Center");
		getContentPane().add(label1, "West"); getContentPane().add(label2, "East");
		getContentPane().add(pa, "South");
		pack();
		server.sCourt.init(point1, point2, this);
		setVisible(true);
		server.sCourt.requestFocus();
		server.sCourt.newGame();
	}



	/**
	  * Avslutar programmet när man trycker på
	  * krysset.
	  */
	class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}

