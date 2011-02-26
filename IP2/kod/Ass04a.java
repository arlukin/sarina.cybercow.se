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
 * S�nder e-post fr�n ett HTLM-formul�r.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass04a extends HttpServlet {
	/*
	 * H�mtar v�rden fr�n specifika parametrar. S�nder med hj�lp av dessa
	 * v�rden ett mail till mottagaren samt skriver ut en bekr�ftelse till klienten.
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
				MailMessage msg = new MailMessage(mailhost); // Skapar en instans av objektet MailMessage d�r argumentet �r namnet p� den host som ska anv�ndas som mailserver.
		    	msg.from(from);
		    	msg.to(to);
		    	msg.cc(cc);
		    	msg.bcc(bcc);
		    	msg.setSubject(subject);

		    	PrintStream body = msg.getPrintStream(); // Anv�nds f�r att skicka meddelandet.
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
		    	out.println("Observera! Detta meddelande �r s�nt fr�n ett formul�r p� Internet och avs�ndaren kan vara felaktig!");

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
