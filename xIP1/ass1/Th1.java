/*
 * @Th1.java 1.0 03/07/29
 *
 */

import javax.swing.*;

/*
 * En tråd som implementerar gränssnittet
 * Runnable.
 *
 * @version 	1.0 29 Juli 2003
 * @author 	Carina Nerén
 */
public class Th1 implements Runnable {
	public Thread activity = new Thread(this);
	public JTextArea area;
	private String text;
	private  long interval;

	/* Skapar en instans av Th1 som har en sträng
	 * med text som ska skrivas ut, en long som är
	 * intervallet vilket texten ska skrivas ut med
	 * samt textarean där texten ska skrivas ut på.
	 */
	public Th1(String txt, long time, JTextArea ar) {
		text = txt;
		interval = time*1000;
		area = ar;
	}



	 /*
	  * Denna metod sätter activity till null
	  * och talar därmed om att huvudloopen i
	  * run metoden ska avbrytas.
	  */
	public void stopIt() {
		activity = null;
	}



     /* Kör tråden Th1 samt skriver ut
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