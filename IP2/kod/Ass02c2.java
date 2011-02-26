/*
 * @Ass02c2.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Använder kakor för att spara ett tillstånd.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass02c2 extends HttpServlet {
	private static String htmlTemplate = null;

	/*
	 * Laddar en HTML-fil som information ska visas på. Hämtar kakorna och adderar de
	 * till html-filen, därefter skickas filen till klienten.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass02c3.html")));
		}
  	   	res.setContentType("text/html");
	   	PrintWriter out = res.getWriter();
	   	Mixer m = new Mixer(htmlTemplate);

	   	Cookie[] cookies = req.getCookies();
	   	if (cookies !=null) {
			for (int i = 0; i < cookies.length; i++) {
				String name = cookies[i].getName();
				String value = cookies[i].getValue();

				if (name.equals("time")) {
					m.add("---$name1---", "" + (name));
	   				m.add("---$value1---", "" + (value));
				} else {
					m.add("---$name2---", "" + (name));
	   				m.add("---$value2---", "" + (value));
				}
			 }
	   		 out.println(m.getMix());
	   	}
	}
}