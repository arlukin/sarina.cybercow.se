/*
 * @Ass04a.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.MailMessage;

/*
 * Sänder e-post från ett HTLM-formulär.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass04a extends HttpServlet {
	/*
	 * Hämtar värden från specifika parametrar. Sänder med hjälp av dessa
	 * värden ett mail till mottagaren samt skriver ut en bekräftelse till klienten.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		String mailhost = "mail.cybercow.se";
		String from = req.getParameter("from");
		String to = req.getParameter("to");
		String cc = req.getParameter("cc");
		String bcc = req.getParameter("bcc");
		String subject = req.getParameter("subject");
		String message = req.getParameter("message");
		String password = req.getParameter("password");

		res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        ServletContext sc = getServletContext();

        String majorversion = Integer.toString(sc.getMajorVersion());
		String minorversion = Integer.toString(sc.getMinorVersion());
		String contentType = req.getContentType();
		Date date = new Date();
		String myDate = date.toString();

		if (password.equals("1234")) {
			try {
				MailMessage msg = new MailMessage(mailhost); // Skapar en instans av objektet MailMessage där argumentet är namnet på den host som ska användas som mailserver.
		    	msg.from(from);
		    	msg.to(to);
		    	msg.cc(cc);
		    	msg.bcc(bcc);
		    	msg.setSubject(subject);

		    	PrintStream body = msg.getPrintStream(); // Används för att skicka meddelandet.
				body.println(message); // Huvudet av mailets meddelande skrivs till body.

		    	Enumeration enum = req.getParameterNames();
				 while (enum.hasMoreElements()) {
				   String name = (String)enum.nextElement();
				   String value = req.getParameter(name);
				   body.println(name + " = " + value);
				 }

		    	msg.sendAndClose();

		    	out.println("Uppgift 4a" + "\r\n");
		    	out.println("MAIL SENT:" + "\r\n");
		    	out.println("To: " + to + "\r\n");
		    	out.println("Cc: " + cc + "\r\n");
		    	out.println("Bcc: " + bcc + "\r\n");
		    	out.println("From: " + from + "\r\n");
		    	out.println("Subject: " + subject + "\r\n");
		    	out.println("Date: " + myDate + "\r\n");
		    	out.println(message + "\r\n");
		    	out.println("Observera! Detta meddelande är sänt från ett formulär på Internet och avsändaren kan vara felaktig!");

		 	} catch (IOException e) {
				out.println("There was a problem handling the submission...");
		 	    log("There was a problem sending email", e);
		 	    e.printStackTrace();
    	 	}
		} else {
			out.println("Uppgift 4a" + "\r\n");
			out.println("Ogiltigt password!!");
		}
    }
}
