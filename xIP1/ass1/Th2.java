/*
 * @Th2.java 1.0 03/07/29
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
public class Th2 extends Thread {
	private boolean bo;
	private String text;
	public JTextArea area;
	private  long interval;

    /* Skapar en instans av Th2 som har en str�ng
	 * med text som ska skrivas ut, en long som �r
	 * intervallet vilket texten ska skrivas ut med
	 * samt textarean d�r texten ska skrivas ut p�.
	 */
	public Th2(String txt, long time, JTextArea ar) {
		text = txt;
		interval = time*1000;
		area = ar;
		bo = false;
	}



	 /*
	  * Denna metod ser till att huvudloopen
	  * i run metoden startas.
	  */
	public void startIt() {
		bo = true;

	}



	 /*
	  * Denna metod ser till att huvudloopen
	  * i run metoden abryts.
	  */
	public void stopIt() {
		bo = false;
	}



	 /* K�r tr�den Th2 samt skriver ut
      * texten text i textarean.
      */
	public void run() {
		while (bo) {
			try {
				sleep(interval);
			} catch(InterruptedException e) {
				break;
			}
			area.append(text + "\n");
		}
	}
}