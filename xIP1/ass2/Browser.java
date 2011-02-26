/*
 * @Browser.java 1.0 03/07/30
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*
 * Denna klass implementerar en vanlig browser
 * genom att presentera informationen fr�n HTTP-
 * adressen som ren text.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Ner�n
 */
class Browser extends JFrame {
	private JButton openButton;
	private JTextField urlName;
	private JTextArea area;
	private String name;

	/*
	 * Konstruktor som skapar ett grafiskt
	 * anv�ndargr�nssnitt.
	 */
	Browser() {
		super("URL-koppling, otolkad information");
		JPanel up = new JPanel();
		up.setBackground(new Color(155,0,155));
		urlName = new JTextField(30);
		urlName.setBackground(new Color(255,175,255));
		urlName.setForeground(Color.black);
		up.add(urlName);
		up.add(openButton = new JButton("Open"));
		getContentPane().add(up, "North");

		area = new JTextArea();
		area.setBackground(new Color(255,175,255));
		area.setForeground(new Color(155,0,155));
		JScrollPane scroll = new JScrollPane(area);
		getContentPane().add(scroll, "Center");

		addWindowListener(new CrossListener());
		OpenListener ol = new OpenListener();
		openButton.addActionListener(ol);
		urlName.addActionListener(ol);

		setLocation(400,300);
		setSize(700,500);
		show();
	}



	/*
	 * En lyssnare som �r kopplad till f�nstrets
	 * kryss och som st�nger f�nstret n�r man
	 * trycker p� krysset.
	 */
	public class CrossListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}



	/*
	 * En lyssnare som �r kopplad till open knappen
	 * och som h�mtar HTTP adressen i textf�ltet samt
	 * visar informationen  fr�n adressen i textarean.
	 */
	public class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				name = urlName.getText();
				if (name.startsWith("www"))
					urlName.setText("http://"+name);
				name = urlName.getText();
				URL url = new URL(name);
				BufferedReader br = new BufferedReader(new
				InputStreamReader(url.openStream()));

				String rad;
				area.setText("");
				while((rad=br.readLine())!=null) {
					area.append(rad);
					area.setWrapStyleWord(true);
					area.append("\n");
				}
			} catch(IOException e) {
				area.append("Fel vid l�sning av URL adressen:  " + name + "\n");
			}
		}
	}



	public static void main(String[]args) {
		new Browser();
	}
}

