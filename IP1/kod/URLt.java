/*
 * @URLt.java 1.0 03/07/30
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.applet.*;
import javax.swing.JEditorPane.*;

/*
 * Ett �pple implementerar en vanlig browser
 * genom att tolka och presentera informationen
 * fr�n HTTP adressen som ren text.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Ner�n
 */
public class URLt extends JApplet {
	private JButton openButton;
	private JTextField urlName;
	private String name;
	private JEditorPane jep;

	/*
	 * Initiering av �pplet. Skapar ett grafiskt
	 * anv�ndargr�nssnitt.
	 */
	public void init() {
		jep = new JEditorPane();
		jep.setBackground(Color.black);
		jep.setForeground(new Color(64,128,128));
		jep.setEditable(false);
		JScrollPane scroll = new JScrollPane(jep);
		getContentPane().add(scroll, "Center");

		JPanel up = new JPanel();
		up.setBackground(new Color(0,64,64));
		urlName = new JTextField(30);
		//urlName.setBackground();
		urlName.setForeground(Color.black);
		up.add(urlName);
		up.add(openButton = new JButton("Open"));
		getContentPane().add(up, "North");

		OpenListener ol = new OpenListener();
		openButton.addActionListener(ol);
		urlName.addActionListener(ol);
		jep.addHyperlinkListener(new LinkFollower(jep));
	}



	/*
	 * En lyssnare som �r kopplad till knappen openButton
	 * och som h�mtar HTTP adressen i textf�ltet samt
	 * visar informationen  fr�n adressen i textrutan
	 * JPane.
	 */
	public class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			try{
				name = urlName.getText();
				if (name.startsWith("www"))
				urlName.setText("http://"+name);
				name = urlName.getText();
				URL url = new URL(name);

				jep.setText("");
				jep.setPage(url);
			} catch (IOException e) {
				jep.setContentType("text/html");
				jep.setText("<html>Could not load" + name + "</html>");
			}
		}
	}



	/*
	 * Klass som aktiverar l�nkar p� webbsidan som visas
	 * samt g�r de tryckbara.
	 */
	public class LinkFollower implements HyperlinkListener {
		private JEditorPane pane;
		public LinkFollower(JEditorPane pane) {
			this.pane = pane;
		}



		public void hyperlinkUpdate(HyperlinkEvent evt) {
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				try {
					pane.setPage(evt.getURL());
				} catch (Exception e) {
				}
			}
		}
	}
}





