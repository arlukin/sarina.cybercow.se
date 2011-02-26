/*
 * @Ass02b1.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * F�r �ver information mellan flera HTML-formul�r med hj�lp av dolda f�lt.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass02b1 extends HttpServlet {
	private static String htmlTemplate = null;

	/*
	 * H�mtar v�rdet p� en specifik parameter samt laddar den HTML-fil
	 * som informationen ska visas p�. Parametern skrivs till filen som dold.
	 * V�rdet adderas till HTML-filen och filen skickas till klienten.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass02b2.html")));
		}
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		Mixer m = new Mixer(htmlTemplate);
		m.add("---$name---", "" + (name));
		out.println(m.getMix());
	}
}


