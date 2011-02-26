/*
 * @Ass01b.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Sänder en dynamisk mängd data till användaren.
 * Denna mängd visar visar samtliga omgivningsvaribler.
 * Detta värde visas med typen text/plain.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass01b extends HttpServlet {
	/* Anropar doGet med . */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		doGet(request, response);
   }

   /*
    * Hämtar alla olika omgivningsvariabler samt skriver
    * ut namnet och dess värde.
    */
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	   response.setContentType("text/plain");
	   PrintWriter out = response.getWriter();
       ServletContext sc = getServletContext(); // Erbjuder information om serverns mjukvara och dess attribut.
	   Enumeration e = null;					// En uppräkning av strängobjekt.

	   // SERVER DELEN.

	   print("SERVER-SIDE INFORMATION:\n###########################", out);

   	   // Servlet namn.
	   print("SERVLET INFORMATION:", out);
	   print("Servlet name", getServletName(), out);

	   // Servlet init parameters.
	   print("SERVLET INIT PARAMETERS:", out);
	   e = getInitParameterNames();
	   while (e.hasMoreElements()) {
	   	   String key = (String)e.nextElement();
	       String value = getInitParameter(key);
	       print(key, value, out);
	   }

	   // Servlet context init parameters.
	   print("CONTEXT INIT PARAMETERS:", out);
	   e = sc.getInitParameterNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       String value = sc.getInitParameter(key);
	       print(key, value, out);
	   }

	   // Servlet namn, port samt info.
	   print("SERVER INFORMATION:", out);
	   print("Server name", request.getServerName(), out);
	   print("Server port", Integer.toString(request.getServerPort()), out);
	   print("Server info", sc.getServerInfo(), out);

	   // Server attribut.
	   print("SERVER ATTRIBUTES:", out);
	   e = sc.getAttributeNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       Object value = sc.getAttribute(key);
	       print(key, value.toString(), out);
	   }

	   // Server versions.
	   print("RUNTIME VERSIONS:", out);
	   print("Major webserver version", Integer.toString(sc.getMajorVersion()), out);
	   print("Minor webserver version", Integer.toString(sc.getMinorVersion()), out);
	   print("JDK version", System.getProperty("java.version"), out);

	   // CLIENTDELEN.

	   // Client adress, värd, användare samt typ.
	   print("CLIENT-SIDE INFORMATION:\n###########################", out);

	   print("CLIENT INFORMATION:", out);
	   print("Remote address", request.getRemoteAddr(), out);
	   print("Remote host", request.getRemoteHost(), out);
	   print("Remote user", request.getRemoteUser(), out);
	   print("Authentication type", request.getAuthType(), out);

	   // Client headers.
	   print("HEADERS:", out);
	   e = request.getHeaderNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       String value = request.getHeader(key);
	       print(key, value, out);
	   }

	   // Client Request parameters.
	   print("REQUEST PARAMETERS:", out);
	   print("Query string", request.getQueryString(), out);
	   print("Character encoding", request.getCharacterEncoding(), out); // Test
	   print("Content length", Integer.toString(request.getContentLength()), out); // Test
	   print("Content type", request.getContentType(), out); // Test

	   // Client requestparameters, one by one.
	   print("REQUEST PARAMETERS, ONE BY ONE:", out);
	   e = request.getParameterNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       String[] values = request.getParameterValues(key);
	       for(int i = 0; i < values.length; i++) {
	          print(key, values[i], out);
	       }
	   }

	   // Client cookies, one by one.
	   print("COOKIES, ONE BY ONE:", out);
	   Cookie[] cookies = request.getCookies();
	   if(cookies != null) {
	       for (int i = 0; i < cookies.length; i++) {
	          Cookie cookie = cookies[i];
	          print(cookie.getName(), cookie.getValue(), out);
	       }
	   }

	   // Client path and connection information.
	   print("PATH AND CONNECTION INFORMATION:", out);
	   print("Path info", request.getPathInfo(), out);
	   print("Translated path", request.getPathTranslated(), out);
	   print("Request URI", request.getRequestURI(), out);
	   print("Request URL", request.getRequestURL().toString(), out);
	   print("Request scheme", request.getScheme(), out);
	   print("Context path", request.getContextPath(), out);
	   print("Servlet path", request.getServletPath(), out);
	   print("Request protocol", request.getProtocol(), out);
	   print("Request method", request.getMethod(), out);

	   // Client request attributes.
	   print("REQUEST ATTRIBUTES:", out);
	   e = request.getAttributeNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       Object value = request.getAttribute(key);
	       print(key, value.toString(), out);
	   }

	   // Client session information.
	   print("SESSION INFORMATION:", out);
	   print("Requested session ID", request.getRequestedSessionId(), out);
	   HttpSession session = request.getSession(true);
	   print("Session creation time", Long.toString(session.getCreationTime()), out);
	   print("Session ID", session.getId(), out);
	   print("Session last accessed", Long.toString(session.getLastAccessedTime()), out);
	   print("Session max inactive interval", Integer.toString(session.getMaxInactiveInterval()), out);

	   // Client session values.
	   print("SESSION VALUES:", out);
	   e = session.getAttributeNames();
	   while (e.hasMoreElements()) {
	       String key = (String)e.nextElement();
	       Object value = request.getAttribute(key);
	       print(key, value.toString(), out);
	    }
    }



    private void print(String string, PrintWriter out) {
	    out.println();
	    out.println(string);
    }



    private void print(String name, String value, PrintWriter out) {
	    if(value == null) {
	 	    value = "null";
	    }
	    out.println("  " + name + ": " + value);
    }
}



