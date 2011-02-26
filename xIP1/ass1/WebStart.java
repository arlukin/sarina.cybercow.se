/*
 * @WebStart.java 1.0 03/07/29
 *
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Thread.*;

/*
 * Ett äpple med två trådar vilka startas
 * med två knappar och som därefter skriver ut
 * en text på en textarea.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Nerén
 */
public class WebStart extends JFrame {
	private Th1 t1;
	private Th2 t2;
	private JTextArea area;
	private JButton button1;
	private JButton button2;

	/*
	 * Initiering av äpplet. Skapar textarean och
	 * knapparna threadbutton1 och threadbutton2
	 * samt kopplar lyssnare till respektive
	 * knapp.
	 */

	WebStart() {
		super("Webstart");
		area = new JTextArea();
		area.setBackground(new Color(255,175,255));
		area.setForeground(new Color(155,0,155));
		JScrollPane scroll = new JScrollPane(area);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scroll, "Center");

		JPanel down = new JPanel();
		down.setBackground(new Color(155,0,155));
		down.add(button1 = new JButton("Aktivera tråd 1"));
		down.add(button2 = new JButton("Aktivera tråd 2"));
		getContentPane().add(down,"South");

		button1.addActionListener(new Listener1());
		button2.addActionListener(new Listener2());

		setSize(400,500);
		show();

	}



        /*
         * En lyssnare kopplad till knappen threadButton1
         * som hämtar knapptexten och beroende på vad som
         * står på knappen startas eller avbryts tråden Thread1.
         */
		class Listener1 implements ActionListener {
			public void actionPerformed(ActionEvent ae) {

				String s = button1.getText();

				if (s.equals("Aktivera tråd 1")) {
					t1 = new Th1("Utskrift från tråd 1", 1, area);
					t1.activity.start();
					button1.setText("Dektivera tråd 1");
					button1.setBackground(Color.gray);
				} else {
					t1.stopIt();
					button1.setText("Aktivera tråd 1");
					button1.setBackground(Color.lightGray);
				}
			}
		}



        /*
		 * En lyssnare kopplad till knappen threadButton2
		 * som hämtar knapptexten och beroende på vad som
		 * står på knappen startas eller avbryts tråden Thread2.
         */
		class Listener2 implements ActionListener {
			public void actionPerformed(ActionEvent ae) {

				String s = button2.getText();

				if (s.equals("Aktivera tråd 2")){

					t2 = new Th2("Utskrift från tråd 2", 1, area);
					t2.startIt();
					t2.start();
					button2.setText("Dektivera tråd 2");
					button2.setBackground(Color.gray);
				} else {
					t2.stopIt();
					button2.setText("Aktivera tråd 2");
					button2.setBackground(Color.lightGray);
				}
			}
		}



		public static void main(String[]args) {
				new WebStart();
	}
}





