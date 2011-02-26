/**
  * @SetName.java 1.0 03/11/12
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
  * Adderar ett specifikt användarnamn som dolt till en HTML-fil.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Nerén
  */
public class SetName extends HttpServlet {
  	private File f;
  	private String type;
  	private long length;
  	private String fileName;
  	private static String htmlTemplate = null;
  	private String str;

	/** Hämtar värdet på en specifik parameter. */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
	    String userName = request.getParameter("username");

	    printToFileUpload(out, userName);
	}


	/** Adderar användarnamnet till en HTML-fil samt skriver ut filen. */
	private void printToFileUpload(PrintWriter out, String userName) {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("FileUpload.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("---$username---", "" + (userName));
		out.println(m.getMix());
	}
}