/*
 * @Browser.java 1.0 03/12/09
 *
 */

import javax.swing.event.*;
import java.awt.event.*;
import javax.net.ssl.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.*;

/*
 * Denna klass implementerar en vanlig browser
 * genom att presentera informationen fr�n HTTP-
 * adressen som ren text. Anv�nder s�ker �verf�ring
 * med SSL.
 *
 * @version 	1.0 9 December 2003
 * @author 	Carina Ner�n
 */
class Browser extends JFrame {
	private JButton openButton;
	private JTextField urlName;
	private JTextArea area;
	private String name;

	/*
	 * Skapar en instans av klassen Browser som skapar ett grafiskt
	 * anv�ndargr�nssnitt.
	 */
	Browser() {
		super("URL-koppling, otolkad information");
		JPanel up = new JPanel();
		up.setBackground(new Color(64,128,128));
		urlName = new JTextField(30);
		urlName.setBackground(new Color(116,186,186));
		urlName.setForeground(Color.black);
		up.add(urlName);
		up.add(openButton = new JButton("Open"));
		getContentPane().add(up, "North");

		area = new JTextArea();
		area.setBackground(new Color(116,186,186));
		area.setForeground(new Color(64,128,128));
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
	 * Verifierar tv� ip-adresser med varandra och ser s�
	 * att de �r lika.
	 */
	class HttpClientVerifier implements HostnameVerifier {
		public boolean verify(String url, SSLSession session) {
			try {
				InetAddress iaU = InetAddress.getByName(url);
				String host = session.getPeerHost();
		        InetAddress iaC = InetAddress.getByName(host);
		        return iaU.equals(iaC);
		    } catch (Exception e) {
		        return false;
			}
		}
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
	 * H�r s�tts ocks� hostens verifierar namn.
	 */
	public class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try {
				name = urlName.getText();
				if (name.startsWith("www"))
					urlName.setText("http://" + name);
				name = urlName.getText();
				URL url = new URL(name);

				HttpsURLConnection huc = (HttpsURLConnection) url.openConnection();
				huc.setHostnameVerifier(new HttpClientVerifier());

				BufferedReader br = new BufferedReader(new InputStreamReader(huc.getInputStream()));
				String s = br.readLine();
				area.setText("");
				while (s != null) {
					area.append(s);
					area.setWrapStyleWord(true);
					area.append("\n");
					s = br.readLine();
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

