/**
  * @SetId.java 1.0 03/11/12
  *
  */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
import java.io.OutputStream.*;

/**
  * L�gger in en ny anv�ndare i databasen samt skriver ut dennes egna sida.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Ner�n
  */
// Laddar upp filer fr�n klienten till servern.
public class SetId extends HttpServlet {
  	private File f;
  	private String type;
  	private long length;
  	private String fileName;
  	private static String htmlTemplate = null;
  	String str;

	/** H�mtar v�rdet p� en specifik parameter. */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String userId = request.getParameter("id");

	    printToUserGuestBook(out, userId);
	}


	/** Adderar anv�ndarnamnet till en HTML-fil samt skriver ut filen. */
	private void printToUserGuestBook(PrintWriter out, String userId) {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("UserGuestBook.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("---$userid---", "" + (userId));
		out.println(m.getMix());
	}
}