/*
 * @Ass01e.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Tar emot information från en HTTP-klient via
 * ett HTML-dokument och sänder tillbaka information
 * med typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass01e extends HttpServlet {
	/*
	 * Hämtar värden på specifika parametrar samt
	 * skriver ut de.
	 * Vilka parametrar som hämtas beror på
	 * om man knappen tryckts på eller inte.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      	response.setContentType("text/plain");
      	PrintWriter out = response.getWriter();

      	if(request.getParameter("button")!=null) {
  			String name1 = request.getParameter("name1");
  			out.println("Namnet är: " + name1);

  			String email = request.getParameter("email");
  			out.println("Email addressen är: " + email);

  			String button = request.getParameter("button");
  			out.println("Värdet hos button är: " + button);

  			String hidden = request.getParameter("hidden");
  			out.println("Värdet av hidden är: " + hidden);
  		} else {
  			String name = request.getParameter("name");
      		out.println("Värdet hos name är: " + name);
      		String email = request.getParameter("email");
      		out.println("Värdet hos email är: " + email);
  		}
  	}

  	/*
  	 * Hämtar namn och värde på alla parametrar samt
  	 * skriver ut det.
  	 */
  	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/plain");
      	PrintWriter out = response.getWriter();

  		Enumeration names = request.getParameterNames();
  		while (names.hasMoreElements()) {
  			String name = (String) names.nextElement();
  			String value[] = request.getParameterValues(name);
  			if(value!=null) {
  				for(int i=0;i<value.length;i++) {
  					out.println("Värdet hos " + name + " är: " + "\n" + value[i] + "\n\r");
  				}
  			}
      	}
  	}
}


