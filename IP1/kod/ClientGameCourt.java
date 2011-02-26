/**
 * @ClientGameCourt.java 1.0 03/09/01
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.*;

/**
  * Skapar en spelplan till klienten.
  *
  * @version 	1.0 1 September 2003
  * @author 	Carina Ner�n
  */
public class ClientGameCourt extends Canvas {
	public ClientPiong clientPiong;
	public JLabel point1;
	public JLabel point2;
	public int xStep, yStep;								// bollens stegl�ngd
	public boolean bo;
	private int currentP1; 									// spelare 1:s aktuella po�ng
	private int currentP2;									// spelare 2:s aktuella po�ng
	private int xMax, yMax; 								// spelarens h�gsta x och y-koordinat
	private int r, x0, y0;									// bollens radie och mittpunkt
	private int v, v0 = 5;									// bollens hastighet
	private int rLe, rRi;									// y-koordinat f�r rackets �vre kant,
	private int rL, rStep;									// y-koordinat f�r rackets l�ngd och stegl�ngd
	private Image image2;									// f�r dubbelbuffring
	private Graphics g2;									// anv�nds f�r image2

	/**
	  * G�r initieringar. Som parametrar f�s tv� stycken
	  * label samt en referens av objektet ClientPiong.
	  *
	  * @param p1 en referens till det Label-objekt som visar po�ngen f�r spelare 1
	  		   p2 en referens till det Label-objekt som visar po�ngen f�r spelare 2
	  		   cp en referens till objektet ClientPiong
	  */
	public void init(JLabel p1, JLabel p2, ClientPiong cp) {
		bo = false;
		clientPiong = cp;
		point1 = p1;
		point2 = p2;
		xMax = getSize().width-1;
		yMax = getSize().height-1;
		r = yMax/20;										// ber�kna bollens radie
		rL = 3*r;											// ber�kna rackets l�ngd
		rStep = r;											// ber�kna rackets stegl�ngd
		Keylistener kl = new Keylistener();
		addKeyListener(kl);
		Componentlistener cl = new Componentlistener();
		addComponentListener(cl);
		image2 = createImage(xMax+1, yMax+1);				// skapar en osynlig bild f�r dubbelbuffring och f�r att undvika flimmer
		g2 = image2.getGraphics();							// knyter image2 till Graphics objektet som anv�nds vid sj�lva ritningen
		reset();
	}


	/**
	  * S�tter bollens position samt ritar
	  * om spelplanen. Har bollens x och y-
	  * koordinater som parametrar.
	  *
	  * @param x bollens x-koordinat
	  		   y bollens y-koordinat
	  */
	public void setPosition(int x, int y) {
		x0 = x;
		y0 = y;
		repaint();											// anropar update automatiskt.
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
	  * S�tter po�ngen f�r spelare 1 samt ritar om
	  * spelplanen. Har en string som parameter.
	  *
	  * @param s en str�ng som visar v�rdet p� spelare 1:s po�ng
	  */
	public void setPoint1(String s) {
		point1.setText(s);
		repaint();
	}



	/**
	  * S�tter po�ngen f�r spelare 2 samt ritar om
	  * spelplanen. Har en string som parameter.
	  *
	  * @param s en str�ng som visar v�rdet p� spelare 2:s po�ng
	  */
	public void setPoint2(String s) {
		point2.setText(s);
		repaint();
	}



	/**
	  * Nollst�ller po�ngen vid varje ny spelomg�ng
	  * samt s�tter racketerna i mitten p� kortsidorna.
	  */
	private void reset() {
		currentP1 = currentP2 = 0;							// nollst�ller po�ngen
		point1.setText("0");
		point2.setText("0");
		xStep = yStep = v = v0 = 5;							// utg�ngshastighet
		x0 = r + 1;											// s�tt bollen i v�nsterkanten
		y0 = yMax/2;										// i mitten av kortsidan
		rLe = rRi = yMax/2-rL/2;							// placer racketarna i mitten
	}



	/**
	  * Suddar bilden.
	  */
	public void update(Graphics g) {
		g2.setColor(getBackground());
		g2.fillRect(0, 0, xMax+1, yMax+1);					// sudda bild
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
	  * Lyssnar efter tangenttryckningar. Koordinaterna skickas
	  * till servern, och klienten flyttar inte racketen sj�lv.
	  */
	class Keylistener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			Color co = clientPiong.player1.getForeground();
			if (e.getKeyCode() == KeyEvent.VK_UP) {			// h�ger upp
				if (co == Color.black) {					// om co �r svart betyder det att det �r spelare 2:s racket som ska flyttas.
					String riUp1 = "ri_up" + "/";
					String riUp2 = "ri_up";
					String riUp3 = riUp1.concat(riUp2 + "");
					clientPiong.out.println(riUp3);
				} else {
					String leUp1 = "le_up" + "/";
					String leUp2 = "le_up";
					String leUp3 = leUp1.concat(leUp2 + "");
					clientPiong.out.println(leUp3);
				}
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {	// h�ger ner
				if (co == Color.black) {
					String riDown1 = "ri_down" + "/";
					String riDown2 = "ri_down";
					String riDown3 = riDown1.concat(riDown2 + "");
					clientPiong.out.println(riDown3);
				} else {
					String leDown1 = "le_down" + "/";
					String leDown2 = "le_down";
					String leDown3 = leDown1.concat(leDown2 + "");
					clientPiong.out.println(leDown3);
				}
			}
		}
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