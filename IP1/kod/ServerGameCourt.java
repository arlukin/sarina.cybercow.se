/**
 * @ServerGameCourt.java 1.0 03/09/01
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

/**
  * Skapar en spelplan till servern.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Ner�n
  */
public class ServerGameCourt extends Canvas implements Runnable {
	public Thread activity;
	public ServerGameGUI serverGameGUI;
	public JLabel point1;
	public JLabel point2;
	public int r, x0, y0;			// bollens radie och mittpunkt
	public int xStep, yStep;		// bollens stegl�ngd
	public int rLe, rRi;			// y-koordinat f�r rackets �vre kant
	public boolean bo;
	private int currentP1; 			// aktuella po�ng
	private int currentP2;
	private int xMax, yMax; 		// Spelarens h�gsta x och y-koordinat
	private int v, v0 = 5;			// bollens hastighet
	private int rL, rStep;			// y-koordinat f�r rackets l�ngd och stegl�ngd
	Image image2;					// f�r dubbelbuffring
	Graphics g2;					// anv�nds f�r image2

	/**
	  * G�r initieringar. Som parametrar f�s tv� stycken
	  * label samt en referens av objektet ServerGameGUI.
	  *
	  * @param p1 en referens till det Label-objekt som visar po�ngen f�r spelare 1
	  		   p2 en referens till det Label-objekt som visar po�ngen f�r spelare 2
	  		   sgg en referens till objektet ServerGameGUI
	  */
	public void init(JLabel p1, JLabel p2, ServerGameGUI sgg) {
		serverGameGUI = sgg;
		bo = false;
		point1 = p1;
		point2 = p2;
		xMax = getSize().width-1;
		yMax = getSize().height-1;
		r = yMax/20;				// ber�kna bollens radie
		rL = 3*r;					// ber�kna rackets l�ngd
		rStep = r;					// ber�kna rackets stegl�ngd
		image2 = createImage(xMax+1, yMax+1);
		g2 = image2.getGraphics();
		reset();
	}



	/**
	  * S�tter y-koordinaten f�r v�nster racket n�r
	  * den flyttats upp�t.
      */
	public void setLeRUp() {
		rLe = Math.max(0, rLe-rStep);
	}



	/**
	  * S�tter y-koordinaten f�r v�nster racket n�r
	  * den flyttats ned�t.
      */
	public void setLeRDown() {
		rLe = Math.min(yMax-rL, rLe+rStep);
	}



	/**
	  * S�tter y-koordinaten f�r h�ger racket n�r
	  * den flyttats upp�t.
      */
	public void setRiRUp() {
		rRi = Math.max(0, rRi-rStep);
	}



	/**
      * S�tter y-koordinaten f�r h�ger racket n�r
	  * den flyttats ner�t.
      */
	public void setRiRDown() {
		rRi = Math.min(yMax-rL, rRi+rStep);
	}



	/**
	  * S�tter bo till true som talar om att
	  * huvudloopen i run() ska startas.
	  */
	public void startIt() {
		bo = true;
	}



	/**
	  * S�tter bo till false som talar om att
	  * huvudloopen i run() ska stoppas.
	  */
	public void stopIt() {
		bo = false;
	}



	/**
	  * Nollst�ller po�ngen vid varje ny spelomg�ng
      * samt s�tter racketerna i mitten p� kortsidorna.
	  */
	private void reset() {
		currentP1 = currentP2 = 0;							// nollst�ll po�ng
		point1.setText("0");
		point2.setText("0");
		xStep = yStep = v = v0 = 5;							// utg�ngshastighet
		x0 = r + 1;											// s�tt bollen i v�nsterkanten
		y0 = yMax/2;										// i mitten av kortsidan
		rLe = rRi = yMax/2-rL/2;							// placer racketarna i mitten
	}



	/**
	  * Startar spelet
	  */
	public void startGame() {
		if (activity == null) {
			activity = new Thread(this);
			startIt();
			activity.start();
		}
	}



	/**
	  * Pausar spelet.
	  */
	public void pauseGame() {
		if (activity !=null) {
			stopIt();
			activity = null;
		}
	}



	/**
	  * Pausar och nollst�ller spelet sedan
	  * startas spelet p� nytt.
	  */
	public void newGame() {
		pauseGame();
		reset();
		startGame();
	}



	/**
	  * Spelet avslutas.
	  */
	public void exitGame() {
		stopIt();
	}



	/**
	  * Startar aktiviteterna i spelet, dvs
	  * bollen s�tts i r�relse.
	  */
	public void run() {
		while (bo) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			if (x0-r <= 0) {								// �r bollen i v�nsterkanten?
				if (y0<rLe || y0>rLe+rL) {					// miss?
					serverGameGUI.server.broadCast(("pling" + "/"), ("inget"));
					point2.setText(String.valueOf(++currentP2));
					serverGameGUI.server.broadCast(("point2" + "/"), (String.valueOf(currentP2)));

					if (currentP2 == 5) {
						pauseGame();
						serverGameGUI.server.broadCast(("winnerPlayer2" + "/"), ("winnerPlayer2"));
					}
					v = v0;									// �terg� till utg�ngshastighet
				} else										// tr�ff
					v ++;									// �ka hastigheten
				xStep = v;									// flytta �t h�ger n�sta g�ng
			}
			else if (x0+r >= xMax) {						// �r bollen i h�gerkanten?
				if (y0<rRi || y0>rRi+rL) {					// miss?
					serverGameGUI.server.broadCast(("pling" + "/"), ("inget"));
					point1.setText(String.valueOf(++currentP1));
					serverGameGUI.server.broadCast(("point1" + "/"), (String.valueOf(currentP1)));

					if (currentP1 == 5) {
						pauseGame();
						serverGameGUI.server.broadCast(("winnerPlayer1" + "/"), ("winnerPlayer1"));
					}
					v = v0;									// �terg� till utg�ngshastighet
				} else										// tr�ff
					v ++;									// �ka hastigheten
				xStep = -v;									// flytta �t v�nster n�sta g�ng
			}
			if (y0-r<=0 || y0+r>=yMax)						// i �ver- eller underkanten?
				yStep = -yStep;								// byt vertikal riktning

			x0 += xStep;									// flytta bollen horisontellt
			y0 += yStep;									// flytta bollen vertikalt

			String str1 = Integer.toString(x0) + " ";
			String str2 = Integer.toString(y0);
			String string1 = str1.concat(str2 + "");
			serverGameGUI.server.broadCast(("position" + "/"), string1);

			if (x0 < r)										// hamnade bollen f�r l�ngt �t v�nster?
				x0 = r;
			else if (x0 > xMax-r)							// hamnade bollen f�r l�ngt �t h�ger?
				x0 = xMax-r+1;
			if (y0 < r)										// hamnade bollen f�r l�ngt upp?
				y0 = r;
			else if (y0 > yMax-r)							// hamnade bollen f�r l�ngt ner?
				y0 = yMax-r+1;
			repaint();
		}
	}



	/**
	  * Suddar bilden.
	  */
	public void update(Graphics g) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, xMax+1, yMax+1);
		g2.setColor(getForeground());
		paint(g);
	}



	/**
	  * Ritar bollen samt de b�da racketarna.
	  * Detta kopieras sedan till sk�rmen.
	  */
	public void paint(Graphics g) {
		g2.setColor(new Color(103, 76, 163));
		g2.fillOval(x0-r, y0-r, 2*r, 2*r);					// rita bollen
		g2.setColor(Color.white);
		g2.fillRect(0, rLe, 2, rL);							// rita v�nster racket
		g2.fillRect(xMax-1, rRi, 5, rL);					// rita h�ger racket
		g.drawImage(image2, 0, 0, this);					// kopiera till sk�rmen
	}



	/**
	  * Lyssnar efter om f�nstrets storlek �ndras genom
	  * att anv�ndaren drar det.
	  */
	class Componentlistener extends ComponentAdapter {
		public void componentResized(ComponentEvent e) {
			xMax = e.getComponent().getSize().width-1;
			yMax = e.getComponent().getSize().height-1;
			image2 = createImage(xMax+1, yMax+1);
			g2.dispose();
			g2 = image2.getGraphics();
			e.getComponent().requestFocus();
			repaint();
		}
	}
}