/*
 * @Ass01e.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Tar emot information fr�n en HTTP-klient via
 * ett HTML-dokument och s�nder tillbaka information
 * med typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass01e extends HttpServlet {
	/*
	 * H�mtar v�rden p� specifika parametrar samt
	 * skriver ut de.
	 * Vilka parametrar som h�mtas beror p�
	 * om man knappen tryckts p� eller inte.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
      	response.setContentType("text/plain");
      	PrintWriter out = response.getWriter();

      	if(request.getParameter("button")!=null) {
  			String name1 = request.getParameter("name1");
  			out.println("Namnet �r: " + name1);

  			String email = request.getParameter("email");
  			out.println("Email addressen �r: " + email);

  			String button = request.getParameter("button");
  			out.println("V�rdet hos button �r: " + button);

  			String hidden = request.getParameter("hidden");
  			out.println("V�rdet av hidden �r: " + hidden);
  		} else {
  			String name = request.getParameter("name");
      		out.println("V�rdet hos name �r: " + name);
      		String email = request.getParameter("email");
      		out.println("V�rdet hos email �r: " + email);
  		}
  	}

  	/*
  	 * H�mtar namn och v�rde p� alla parametrar samt
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
  					out.println("V�rdet hos " + name + " �r: " + "\n" + value[i] + "\n\r");
  				}
  			}
      	}
  	}
}


