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
  * @author 	Carina Nerén
  */
public class ClientGameCourt extends Canvas {
	public ClientPiong clientPiong;
	public JLabel point1;
	public JLabel point2;
	public int xStep, yStep;								// bollens steglängd
	public boolean bo;
	private int currentP1; 									// spelare 1:s aktuella poäng
	private int currentP2;									// spelare 2:s aktuella poäng
	private int xMax, yMax; 								// spelarens högsta x och y-koordinat
	private int r, x0, y0;									// bollens radie och mittpunkt
	private int v, v0 = 5;									// bollens hastighet
	private int rLe, rRi;									// y-koordinat för rackets övre kant,
	private int rL, rStep;									// y-koordinat för rackets längd och steglängd
	private Image image2;									// för dubbelbuffring
	private Graphics g2;									// används för image2

	/**
	  * Gör initieringar. Som parametrar fås två stycken
	  * label samt en referens av objektet ClientPiong.
	  *
	  * @param p1 en referens till det Label-objekt som visar poängen för spelare 1
	  		   p2 en referens till det Label-objekt som visar poängen för spelare 2
	  		   cp en referens till objektet ClientPiong
	  */
	public void init(JLabel p1, JLabel p2, ClientPiong cp) {
		bo = false;
		clientPiong = cp;
		point1 = p1;
		point2 = p2;
		xMax = getSize().width-1;
		yMax = getSize().height-1;
		r = yMax/20;										// beräkna bollens radie
		rL = 3*r;											// beräkna rackets längd
		rStep = r;											// beräkna rackets steglängd
		Keylistener kl = new Keylistener();
		addKeyListener(kl);
		Componentlistener cl = new Componentlistener();
		addComponentListener(cl);
		image2 = createImage(xMax+1, yMax+1);				// skapar en osynlig bild för dubbelbuffring och för att undvika flimmer
		g2 = image2.getGraphics();							// knyter image2 till Graphics objektet som används vid själva ritningen
		reset();
	}


	/**
	  * Sätter bollens position samt ritar
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
	  * Sätter poängen för spelare 1 samt ritar om
	  * spelplanen. Har en string som parameter.
	  *
	  * @param s en sträng som visar värdet på spelare 1:s poäng
	  */
	public void setPoint1(String s) {
		point1.setText(s);
		repaint();
	}



	/**
	  * Sätter poängen för spelare 2 samt ritar om
	  * spelplanen. Har en string som parameter.
	  *
	  * @param s en sträng som visar värdet på spelare 2:s poäng
	  */
	public void setPoint2(String s) {
		point2.setText(s);
		repaint();
	}



	/**
	  * Nollställer poängen vid varje ny spelomgång
	  * samt sätter racketerna i mitten på kortsidorna.
	  */
	private void reset() {
		currentP1 = currentP2 = 0;							// nollställer poängen
		point1.setText("0");
		point2.setText("0");
		xStep = yStep = v = v0 = 5;							// utgångshastighet
		x0 = r + 1;											// sätt bollen i vänsterkanten
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
	  * Lyssnar efter tangenttryckningar. Koordinaterna skickas
	  * till servern, och klienten flyttar inte racketen själv.
	  */
	class Keylistener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			Color co = clientPiong.player1.getForeground();
			if (e.getKeyCode() == KeyEvent.VK_UP) {			// höger upp
				if (co == Color.black) {					// om co är svart betyder det att det är spelare 2:s racket som ska flyttas.
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
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {	// höger ner
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