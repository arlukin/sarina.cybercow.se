/*
 * @Ass01a.java 1.0 03/11/12
 *
 */

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Denna klass gör en räknare som visar totala antalet besök av sidan.
 * Värdet på antalet träffar är en statisk mängd och lagras i en fil
 * på serversidan. Detta värde visas med typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass01a extends HttpServlet {
  	private static long count = 0;
  	private String fileName = "number_of_hits.txt";
  	private File f;
  	private String initial;

  	/* Hämtar information från den fil som innehåller värdet på antalet träffar. */
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
	      	   	System.err.println("Problem vid läsningen av " + f);
	      	} catch(NumberFormatException nfe) {
				System.err.println("Ej fungerande värde i " + f);
	      	} finally {
				try{
					if(br != null) {
						br.close();
				   	}
	      	   	} catch(IOException ioe) {
				   	System.err.println("Problem vid stängningen av BufferedReader");
	      	   	}
		   	}
	   	}
   	}



   	/* Skriver ut antalet besökare på sidan. */
   	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
   	    PrintWriter out = response.getWriter();

       	synchronized(this) {
			out.println("Antal sidbesök sen start: " + ++count + " stycken.");
	   	}
   	}


   	/* Sparar värdet på antalet träffar på filen. */
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




