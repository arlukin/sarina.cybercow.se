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
  * @author 	Carina Nerén
  */
public class ServerGameCourt extends Canvas implements Runnable {
	public Thread activity;
	public ServerGameGUI serverGameGUI;
	public JLabel point1;
	public JLabel point2;
	public int r, x0, y0;			// bollens radie och mittpunkt
	public int xStep, yStep;		// bollens steglängd
	public int rLe, rRi;			// y-koordinat för rackets övre kant
	public boolean bo;
	private int currentP1; 			// aktuella poäng
	private int currentP2;
	private int xMax, yMax; 		// Spelarens högsta x och y-koordinat
	private int v, v0 = 5;			// bollens hastighet
	private int rL, rStep;			// y-koordinat för rackets längd och steglängd
	Image image2;					// för dubbelbuffring
	Graphics g2;					// används för image2

	/**
	  * Gör initieringar. Som parametrar fås två stycken
	  * label samt en referens av objektet ServerGameGUI.
	  *
	  * @param p1 en referens till det Label-objekt som visar poängen för spelare 1
	  		   p2 en referens till det Label-objekt som visar poängen för spelare 2
	  		   sgg en referens till objektet ServerGameGUI
	  */
	public void init(JLabel p1, JLabel p2, ServerGameGUI sgg) {
		serverGameGUI = sgg;
		bo = false;
		point1 = p1;
		point2 = p2;
		xMax = getSize().width-1;
		yMax = getSize().height-1;
		r = yMax/20;				// beräkna bollens radie
		rL = 3*r;					// beräkna rackets längd
		rStep = r;					// beräkna rackets steglängd
		image2 = createImage(xMax+1, yMax+1);
		g2 = image2.getGraphics();
		reset();
	}



	/**
	  * Sätter y-koordinaten för vänster racket när
	  * den flyttats uppåt.
      */
	public void setLeRUp() {
		rLe = Math.max(0, rLe-rStep);
	}



	/**
	  * Sätter y-koordinaten för vänster racket när
	  * den flyttats nedåt.
      */
	public void setLeRDown() {
		rLe = Math.min(yMax-rL, rLe+rStep);
	}



	/**
	  * Sätter y-koordinaten för höger racket när
	  * den flyttats uppåt.
      */
	public void setRiRUp() {
		rRi = Math.max(0, rRi-rStep);
	}



	/**
      * Sätter y-koordinaten för höger racket när
	  * den flyttats neråt.
      */
	public void setRiRDown() {
		rRi = Math.min(yMax-rL, rRi+rStep);
	}



	/**
	  * Sätter bo till true som talar om att
	  * huvudloopen i run() ska startas.
	  */
	public void startIt() {
		bo = true;
	}



	/**
	  * Sätter bo till false som talar om att
	  * huvudloopen i run() ska stoppas.
	  */
	public void stopIt() {
		bo = false;
	}



	/**
	  * Nollställer poängen vid varje ny spelomgång
      * samt sätter racketerna i mitten på kortsidorna.
	  */
	private void reset() {
		currentP1 = currentP2 = 0;							// nollställ poäng
		point1.setText("0");
		point2.setText("0");
		xStep = yStep = v = v0 = 5;							// utgångshastighet
		x0 = r + 1;											// sätt bollen i vänsterkanten
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
	  * Pausar och nollställer spelet sedan
	  * startas spelet på nytt.
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
	  * bollen sätts i rörelse.
	  */
	public void run() {
		while (bo) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
			if (x0-r <= 0) {								// är bollen i vänsterkanten?
				if (y0<rLe || y0>rLe+rL) {					// miss?
					serverGameGUI.server.broadCast(("pling" + "/"), ("inget"));
					point2.setText(String.valueOf(++currentP2));
					serverGameGUI.server.broadCast(("point2" + "/"), (String.valueOf(currentP2)));

					if (currentP2 == 5) {
						pauseGame();
						serverGameGUI.server.broadCast(("winnerPlayer2" + "/"), ("winnerPlayer2"));
					}
					v = v0;									// återgå till utgångshastighet
				} else										// träff
					v ++;									// öka hastigheten
				xStep = v;									// flytta åt höger nästa gång
			}
			else if (x0+r >= xMax) {						// är bollen i högerkanten?
				if (y0<rRi || y0>rRi+rL) {					// miss?
					serverGameGUI.server.broadCast(("pling" + "/"), ("inget"));
					point1.setText(String.valueOf(++currentP1));
					serverGameGUI.server.broadCast(("point1" + "/"), (String.valueOf(currentP1)));

					if (currentP1 == 5) {
						pauseGame();
						serverGameGUI.server.broadCast(("winnerPlayer1" + "/"), ("winnerPlayer1"));
					}
					v = v0;									// återgå till utgångshastighet
				} else										// träff
					v ++;									// öka hastigheten
				xStep = -v;									// flytta åt vänster nästa gång
			}
			if (y0-r<=0 || y0+r>=yMax)						// i över- eller underkanten?
				yStep = -yStep;								// byt vertikal riktning

			x0 += xStep;									// flytta bollen horisontellt
			y0 += yStep;									// flytta bollen vertikalt

			String str1 = Integer.toString(x0) + " ";
			String str2 = Integer.toString(y0);
			String string1 = str1.concat(str2 + "");
			serverGameGUI.server.broadCast(("position" + "/"), string1);

			if (x0 < r)										// hamnade bollen för långt åt vänster?
				x0 = r;
			else if (x0 > xMax-r)							// hamnade bollen för långt åt höger?
				x0 = xMax-r+1;
			if (y0 < r)										// hamnade bollen för långt upp?
				y0 = r;
			else if (y0 > yMax-r)							// hamnade bollen för långt ner?
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
	  * Ritar bollen samt de båda racketarna.
	  * Detta kopieras sedan till skärmen.
	  */
	public void paint(Graphics g) {
		g2.setColor(new Color(103, 76, 163));
		g2.fillOval(x0-r, y0-r, 2*r, 2*r);					// rita bollen
		g2.setColor(Color.white);
		g2.fillRect(0, rLe, 2, rL);							// rita vänster racket
		g2.fillRect(xMax-1, rRi, 5, rL);					// rita höger racket
		g.drawImage(image2, 0, 0, this);					// kopiera till skärmen
	}



	/**
	  * Lyssnar efter om fönstrets storlek ändras genom
	  * att användaren drar det.
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