/*
 * @Ass02a1.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * F�r �ver information mellan flera HTML-dokument med hj�lp av URL-l�nkar med argument.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass02a1 extends HttpServlet {
	private static String htmlTemplate = null;
	String name;

	/*
	 * H�mtar v�rdet p� en specifik parameter samt laddar den HTML-fil
	 * som informationen ska visas p�. V�rdet adderas till HTML-filen och
	 * filen skickas till klienten.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		name = req.getParameter("name");

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass02a2.html")));
		}
  	   res.setContentType("text/html");
	   PrintWriter out = res.getWriter();

	   Mixer m = new Mixer(htmlTemplate);
	   m.add("---name---", "" + (name));
	   out.println(m.getMix());
	}
}

