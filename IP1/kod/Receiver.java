 /*
  * @Receiver.java 1.0 03/09/04
  *
  */

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;

 /*
  * Denna klass tar emot meddelanden
  * från mottagaren.
  *
  * @version 	1.0 4 September 2003
  * @author 	Carina Nerén
  */
public class Receiver implements Runnable {
	public Thread activity = new Thread(this);
	private DatagramSocket socket;
	private Draw draw;
	private boolean bo = true;

	public Receiver(Draw dr, DatagramSocket so) throws UnknownHostException, IOException {
		draw = dr;
		socket = so;
	}



     /*
      * Denna metod lyssnar efter inkommande
      * meddelanden. Dessa meddelanden görs om
      * till en sträng som delas vid ett mellanslag
      * och dessa två strängar görs om till
      * två integer som motsvarar en points x
      * och y koordinat.
      */
    public void run() {
		byte[] data = new byte[8192];

		while(true) {
			try {
				DatagramPacket incoming = new DatagramPacket(data, data.length);
				socket.receive(incoming);
				String str = new String(incoming.getData(), 0, incoming.getLength());
				String [] strings = str.split(" ");
				int int1 = Integer.parseInt(strings[0]);
				int int2 = Integer.parseInt(strings[1]);
				Point p = new Point(int1, int2);

				draw.d.p.addPoint(p);
				Thread.yield();

			} catch(IOException ie) {
				break;
			}
		}
	}
}