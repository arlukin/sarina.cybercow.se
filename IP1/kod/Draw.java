 /*
  * @Draw.java 1.0 03/09/04
  *
  */

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.net.*;
import java.io.*;
import java.lang.*;
import java.lang.Object.*;
import java.lang.Thread.*;

 /*
  * Denna klass s�nder datagrampaket fr�n ett
  * program till ett annat.
  *
  * @version 	1.0 4 September 2003
  * @author 	Carina Ner�n
  */
public class Draw {
	public Receiver receiver;
	public Draw draw;
	public Draw1 d;
	private InetAddress remoteHost;
	private DatagramSocket socket;
	private int remotePort;
	private int myPort;

	/*
	 * Skapar en instans av Draw d�r parametrarna �r
	 * den port man sj�lv �r ansluten till, samt mottagerens
	 * adress och port.
	 */
	public Draw(int x, InetAddress ia, int y) throws UnknownHostException, IOException {
		myPort = x;
		remoteHost = ia;
		remotePort = y;

		d = new Draw1();							// skapar ett nytt ritobjekt
		socket = new DatagramSocket(myPort);   		// �ppnar en datagramsocket som paket kan s�ndas och tas emot �ver.
		receiver = new Receiver(this, socket);		// skapar ett nytt tr�dobjekt som skickar med DGSocket och socketen.
		receiver.activity.start();					// startar tr�den.
	}



	 /*
	  * Denna metod s�nder ett paket till
	  * en mottagare. Skapar paketet genom att
	  * konvertera punktens x och y koordinat
	  * till tv� str�ngar som sedan sl�s ihop
	  * till en str�ng, och denna str�ng g�rs
	  * om till byte som kan skickas �ver
	  * datagramet som ett paket. Parameter �r
	  * en punkt.
	  */
	public synchronized void send(Point point) {
		try {
			String str1 = Integer.toString(point.x) + " ";
			String str2 = Integer.toString(point.y);
			String string = str1.concat(str2 + "");

			byte[] sendData = string.getBytes();
			DatagramPacket output = new DatagramPacket(sendData, sendData.length, remoteHost, remotePort);
			socket.send(output);

		} catch(SocketException se) {
		} catch(IOException ie) {
		}
	}


	 /*
	  * Ett ritprogram.
	  */
	 public class Draw1 extends JFrame {
	  public Paper p = new Paper();

	  public Draw1() {
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    getContentPane().add(p, BorderLayout.CENTER);

	    setSize(640, 480);
	    show();
	  }
	}



	/*
	 * Skapar en set samt adderar muslyssnare till klasserna
	 * L1 och L2.
	 */
	class Paper extends JPanel {
	  Set set = Collections.synchronizedSet(new HashSet());

	  public Paper() {
	    setBackground(Color.white);
	    addMouseListener(new L1());
	    addMouseMotionListener(new L2());
	  }



	  /* Ritar punkterna. */
	  public synchronized void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(new Color(155,0,155));
	    Iterator i = set.iterator();
	    while(i.hasNext()) {
	      Point p = (Point)i.next();
	      g.fillOval(p.x, p.y, 2, 2);
	    }
	  }



	  /* Adderar en punkt till set. */
	  public synchronized void addPoint(Point p) {
	    set.add(p);
	    repaint();
	  }



	  /*
	   * Lyssnar efter att musen trycks ned, d�refter h�mtas
	   * punkten d�r musen trycktes ner och sedan anropas metoderna
	   * addPoint samt send med denna punkt som argument.
	   */
	  class L1 extends MouseAdapter {
	    public void mousePressed(MouseEvent me) {
	      addPoint(me.getPoint());
	      send(me.getPoint());
	    }
	  }



	  /*
	   * Lyssnar efter n�r musen dras, h�mtar d�refter den punkt
	   * d�r musen drogs och sedan anropas metoderna addPoint samt
	   * send med denna punkt som argument.
	   */
	  class L2 extends MouseMotionAdapter {
	    public void mouseDragged(MouseEvent me) {
	      addPoint(me.getPoint());
	      send(me.getPoint());
	    }
	  }
	}



	public static void main(String[]args) throws UnknownHostException, IOException {
		if (args.length == 3) {
			int myPort = Integer.parseInt(args[0]);
			InetAddress remoteHost = InetAddress.getByName(args[1]);
			int remotePort = Integer.parseInt(args[2]);

			new Draw(myPort, remoteHost, remotePort);
		}
	}
}


