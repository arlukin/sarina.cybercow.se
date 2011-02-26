/*
 * @Ass02b2.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * F�r den slutgiltiga informationen och skriver ut den med
 * typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass02b2 extends HttpServlet {
	/* H�mtar v�redena p� de specifika parametrarna samt skriver ut de. */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String button = req.getParameter("button");

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		out.println("Uppgift 2b\n\n");
		out.println("V�rdet hos name �r:\n");
		out.println(name + "\n\n");
		out.println("V�rdet hos email �r:\n");
		out.println(email + "\n\n");
		out.println("V�rdet hos button �r:\n");
		out.println(button + "\n\n");
	}
}


