/*
 * @Signed_Browser.java 1.0 03/11/26
 *
 */

import javax.swing.JEditorPane.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.*;

/*
 * Ett äpple implementerar en vanlig browser
 * genom att tolka och presentera informationen
 * från HTTP adressen som ren text.
 *
 * @version 	1.0 26 November 2003
 * @author 	Carina Nerén
 */
public class Signed_Browser extends JApplet {
	private JButton openButton;
	private JTextField urlName;
	private JEditorPane jep;
	private String name;


	/*
	 * Initiering av äpplet. Skapar ett grafiskt
	 * användargränssnitt.
	 */
	public void init() {
		jep = new JEditorPane();
		jep.setBackground(new Color(116,186,186));
		jep.setForeground(new Color(64,128,128));
		jep.setEditable(false);
		JScrollPane scroll = new JScrollPane(jep);
		getContentPane().add(scroll, "Center");

		JPanel up = new JPanel();
		up.setBackground(new Color(0,64,64));
		urlName = new JTextField(30);
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
	 * En lyssnare som är kopplad till knappen openButton
	 * och som hämtar HTTP adressen i textfältet samt
	 * visar informationen  från adressen i textrutan
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
	 * Klass som aktiverar länkar på webbsidan som visas
	 * samt gör de tryckbara.
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





