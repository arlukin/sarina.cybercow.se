/*
 * @Apple.java 1.0 03/07/29
 *
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.applet.*;
import java.lang.Thread.*;

/*
 * Ett �pple med tv� tr�dar vilka startas
 * med tv� knappar och som d�refter skriver ut
 * en text p� en textarea.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Ner�n
 */
public class Apple extends JApplet {
	private Thread1 t1;
	private Thread2 t2;
	private JTextArea area;
	private JButton threadButton1;
	private JButton threadButton2;

	/*
	 * Initiering av �pplet. Skapar textarean och
	 * knapparna threadbutton1 och threadbutton2
	 * samt kopplar lyssnare till respektive
	 * knapp.
	 */
	public void init() {
		area = new JTextArea();
		area.setBackground(new Color(255,175,255));
		area.setForeground(new Color(155,0,155));
		JScrollPane scroll = new JScrollPane(area);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		JPanel down = new JPanel();
		down.setBackground(new Color(155,0,155));
		down.setLayout(new FlowLayout());
		down.add(threadButton1 = new JButton("Aktivera tr�d 1"));
		down.add(threadButton2 = new JButton("Aktivera tr�d 2"));

		getContentPane().add(scroll, "Center");
		getContentPane().add(down,"South");

		threadButton1.addActionListener(new ThreadListener1());
		threadButton2.addActionListener(new ThreadListener2());
	}



    /*
     * En lyssnare kopplad till knappen threadButton1
     * som h�mtar knapptexten och beroende p� vad som
     * st�r p� knappen startas eller avbryts tr�den Thread1.
     */
	class ThreadListener1 implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			String s = threadButton1.getText();

			if (s.equals("Aktivera tr�d 1")) {
				t1 = new Thread1("Utskrift fr�n tr�d 1", 1, area);
				t1.activity.start();
				threadButton1.setText("Dektivera tr�d 1");
				threadButton1.setBackground(Color.gray);
			} else {
				t1.stopIt();
				threadButton1.setText("Aktivera tr�d 1");
				threadButton1.setBackground(Color.lightGray);

			}
		}
	}



   /*
	* En lyssnare kopplad till knappen threadButton2
	* som h�mtar knapptexten och beroende p� vad som
	* st�r p� knappen startas eller avbryts tr�den Thread2.
    */
	class ThreadListener2 implements ActionListener {
		public void actionPerformed(ActionEvent ae) {

			String s = threadButton2.getText();

			if (s.equals("Aktivera tr�d 2")){

				t2 = new Thread2("Utskrift fr�n tr�d 2", 1, area);
				t2.startIt();
				t2.start();
				threadButton2.setText("Dektivera tr�d 2");
				threadButton2.setBackground(Color.gray);
			} else {
				t2.stopIt();
				threadButton2.setText("Aktivera tr�d 2");
				threadButton2.setBackground(Color.lightGray);
			}
		}
	}
}





