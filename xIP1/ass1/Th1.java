/*
 * @Th1.java 1.0 03/07/29
 *
 */

import javax.swing.*;

/*
 * En tr�d som implementerar gr�nssnittet
 * Runnable.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Ner�n
 */
public class Th1 implements Runnable {
	public Thread activity = new Thread(this);
	public JTextArea area;
	private String text;
	private  long interval;

	/* Skapar en instans av Th1 som har en str�ng
	 * med text som ska skrivas ut, en long som �r
	 * intervallet vilket texten ska skrivas ut med
	 * samt textarean d�r texten ska skrivas ut p�.
	 */
	public Th1(String txt, long time, JTextArea ar) {
		text = txt;
		interval = time*1000;
		area = ar;
	}



	 /*
	  * Denna metod s�tter activity till null
	  * och talar d�rmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void stopIt() {
		activity = null;
	}



     /* K�r tr�den Th1 samt skriver ut
      * texten text i textarean.
      */
    public void run() {
		Thread myThread = Thread.currentThread();

		while (activity == myThread){
			try {
				Thread.sleep(interval);
			} catch(InterruptedException e) {
				break;
			}
			area.append(text + "\n");
		}
	}
}