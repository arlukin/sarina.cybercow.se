/*
 * @Ass01a.java 1.0 03/11/12
 *
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Denna klass g�r en r�knare som visar totala antalet bes�k av sidan.
 * V�rdet p� antalet tr�ffar �r en statisk m�ngd och lagras i en fil
 * p� serversidan. Detta v�rde visas med typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass01a extends HttpServlet {
  	private static long count = 0;
  	private String fileName = "number_of_hits.txt";
  	private File f;
  	private String initial;

  	/* H�mtar information fr�n den fil som inneh�ller v�rdet p� antalet tr�ffar. */
  	public void init() throws ServletException {
		f = new File(getServletContext().getRealPath(fileName));
	    BufferedReader br = null;

	    synchronized(this) {
	      	try {
				FileReader fr = new FileReader(f.getAbsolutePath());
	      	   	br = new BufferedReader(fr);
	      	   	initial = br.readLine();
				count = Integer.parseInt(initial);
	      	} catch(FileNotFoundException fnfe) {
	      	   	System.err.println("Kunde inte hitta " + f);
	      	} catch(IOException ioe) {
	      	   	System.err.println("Problem vid l�sningen av " + f);
	      	} catch(NumberFormatException nfe) {
				System.err.println("Ej fungerande v�rde i " + f);
	      	} finally {
				try{
					if(br != null) {
						br.close();
				   	}
	      	   	} catch(IOException ioe) {
				   	System.err.println("Problem vid st�ngningen av BufferedReader");
	      	   	}
		   	}
	   	}
   	}



   	/* Skriver ut antalet bes�kare p� sidan. */
   	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
   	    PrintWriter out = response.getWriter();

       	synchronized(this) {
			out.println("Antal sidbes�k sen start: " + ++count + " stycken.");
	   	}
   	}


   	/* Sparar v�rdet p� antalet tr�ffar p� filen. */
   	public void destroy() {
	   	FileWriter in = null;
	   	PrintWriter out = null;
	   	synchronized (this) {
		   	try {
			   	in = new FileWriter(f);
			   	out = new PrintWriter(in);
			   	out.println(count);
		   	} catch(IOException ioe) {
	           	System.err.println("Problem med att skriva till: " + f);
	       	} finally {
	           	if(out != null) {
				   	out.flush();
				   	out.close();
			   	}
		   	}
	   	}
   	}
}




