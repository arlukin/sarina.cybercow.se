/*
 * @Ass06a.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Denna klass gör en räknare som visar totala antalet besök av sidan.
 * Värdet på antalet träffar är en statisk mängd och lagras i en fil
 * på serversidan. Värdet sänds som HTML till användaren. Sidan använder
 * klientstyrd omladdning.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass06a extends HttpServlet {
	private static long count = 0;
	private static String htmlTemplate = null;
    private String fileName = "number_of_hits.txt";
    private File f;
    private String initial;

    /*
	 * Hämtar information från den fil som innehåller värdet på antalet träffar.
	 * Laddar den HTML-fil som värdet på antalet träffar ska visas på
	 * samt returnerar innehållet som en sträng.
     */
    public void init() throws ServletException {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass06a.html")));
		}
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



    /*
     * Skriver ut antalet besökare samt tiden på sidan till den hämtade HTML-filen samt
     * uppdaterar sidan var fjärde sekund.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      	response.setContentType("text/html");
      	PrintWriter out = response.getWriter();
		String myDate = new Date().toString();
		response.setHeader("Refresh", "4");

		Mixer m = new Mixer(htmlTemplate);
		m.add("---time---", "" + (myDate));

      	count++;
		m.add("---hits---", "" + (count));

		out.println(m.getMix());
  	}


  	/* Sparar värdet på antalet träffar på samma fil .*/
  	public void destroy() {
  	    FileWriter in = null;
  	    PrintWriter out = null;
  	    synchronized (this) {
  			try {
  				in = new FileWriter(f);
  				out = new PrintWriter(in);
  				out.println(count);
  				return;
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
