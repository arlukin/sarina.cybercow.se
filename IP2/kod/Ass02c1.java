/*
 * @Ass02c1.java 1.0 03/11/12
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
public class Ass02c1 extends HttpServlet {
	private static String htmlTemplate = null;

	/*
	 * Hämtar värdet på en specifik parameter samt laddar den HTML-fil
	 * som informationen ska visas på. Sätter två kakor hos klienten, en med tid och
	 * en med namn. Dessutom sätts livslängden på kakorna.
	 * Värdet adderas till HTML-filen och filen skickas till klienten.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass02c2.html")));
		}
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		Date date = new Date();
		String myDate = date.toString();

		Cookie cookieTime = new Cookie("time", myDate);
		res.addCookie(cookieTime);
		Cookie cookieName = new Cookie("name", name);
		res.addCookie(cookieName);

		cookieTime.setMaxAge(10800);
		cookieName.setMaxAge(10800);

		Mixer m = new Mixer(htmlTemplate);
		m.add("---$name1---", "" + ("time"));
		m.add("---$value1---", "" + (myDate));
		m.add("---$name2---", "" + ("name"));
		m.add("---$value2---", "" + (name));
		out.println(m.getMix());
	}
}

