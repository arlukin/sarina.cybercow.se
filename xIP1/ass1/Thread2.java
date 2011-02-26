/*
 * @Thread2.java 1.0 03/07/29
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
public class Thread2 extends Thread {
	public JTextArea area;
	private boolean bo;
	private String text;
	private  long interval;

    /* Skapar en instans av Thread2 som har en str�ng
	 * med text som ska skrivas ut, en long som �r
	 * intervallet vilket texten ska skrivas ut med
	 * samt textarean d�r texten ska skrivas ut p�.
	 */
	public Thread2(String txt, long time, JTextArea ar) {
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



	/*
	  *K�r tr�den Thread2 samt skriver
	  * ut texten text i textarean.
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