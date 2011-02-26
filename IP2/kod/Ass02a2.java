/*
 * @Ass02a2.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Får den slutgiltiga informationen och skriver ut den med
 * typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass02a2 extends HttpServlet {
	/* Hämtar väredena på de specifika parametrarna samt skriver ut de. */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");
		String email = req.getParameter("email");

		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();

		out.println("Uppgift 2a\n\n");
		out.println("Värdet hos name är: " + "\n");
		out.println(name + "\n\n");
		out.println("Värdet hos email är: " + "\n");
		out.println(email + "\n\n");
	}
}
