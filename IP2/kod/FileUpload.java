/**
  * @FileUpload.java 1.0 03/11/12
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
  * Laddar upp filer från klienten till servern.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Nerén
  */
public class FileUpload extends HttpServlet {
  	private File f;
  	private String type;
  	private long length;
  	private String fileName;
  	private static String htmlTemplate = null;
  	private String str;

  	private String db_name = "db_03_carina_n";
	private String db_username = "db_03_carina_n";
	private String db_password = "691995";
	private String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	/**
	  * Skapar ett multirequest för att underlätta läsning av informationen i filen.
      * Sätter övre gränsen för hur mycket information som ska tas emot till 5G.
	  * Användarnamnet på användaren hämtas samt filnamn, filtyp samt filstorlek.
	  */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();
    	try {
			MultipartRequest multi = new MultipartRequest(request, "../webapps/ROOT", 5 * 1024 * 1024);
	        String username = multi.getParameter("username");

			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String)files.nextElement();
			    fileName = multi.getFilesystemName(name);
			    String original = multi.getOriginalFileName(name);
			    type = multi.getContentType(name);
			    f = multi.getFile(name);
			}

			handleForm(fileName, username);
			getUser(out, username);
		 } catch (Exception e) {
			 e.printStackTrace(out);
		 }
	 }


	 /** Uppdaterar databasen med bildfilen hos den specifike användaren. */
	 public void handleForm(String fileName, String username) {
		 Connection dbConnection = null;
	     Statement stmt = null;
	     try {
			 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
	 		 dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
	 		 stmt = dbConnection.createStatement();
	 		 stmt.executeUpdate("UPDATE user SET picture=\"" + fileName + "\" WHERE username=\"" + username + "\"");
	     } catch (Exception e) {
			 e.printStackTrace();
	     } finally {
			 try {
				 if (dbConnection != null)
	 			   dbConnection.close();
	 			   dbConnection = null;
	         } catch (SQLException ignored) {
				 ignored.printStackTrace();
	 		 }
	 	 }
     }


   	 /** Skriver ut användarens egna sida, men den här gången med den uppladdade filen. */
     private void getUser(PrintWriter out, String username) {
	     Connection dbConnection = null;
   	     Statement stmt = null;
   	     ResultSet rs = null;

   	     if(htmlTemplate == null) {
		     htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("MyPage.html")));
   	     }

   	     try {
   		 	 Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
   			 dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
   			 stmt = dbConnection.createStatement();
   			 rs = stmt.executeQuery("SELECT username, password, email, homepage, presentation, icq, picture, name FROM user WHERE username=\"" + username + "\"");
   			 Mixer m = new Mixer(htmlTemplate);

   			 while (rs.next()) {
				 String password = rs.getString("password");
				 String email = rs.getString("email");
				 String homepage = rs.getString("homepage");
				 String presentation = rs.getString("presentation");
				 String icq = rs.getString("icq");
				 String picture = rs.getString("picture");
				 String name = rs.getString("name");

				 m.add("---username---", "" + (username));
				 if (email == null) {
				 	 m.add("---email---", "" + "ingen email ännu");
				 } else {
					 m.add("---email---", "" + (email));
				 }
				 if (homepage == null) {
					 m.add("---homepage---", "" + "ingen hemsida ännu");
				 } else {
					 m.add("---homepage---", "" + (homepage));
				 }
				 if (presentation == null) {
					 m.add("---presentation---", "" + "ingen presentation ännu");
				 } else {
					 m.add("---presentation---", "" + (presentation));
				 }
				 if (icq == null) {
					 m.add("---icq---", "" + "ingen icq ännu");
				 } else {
					 m.add("---icq---", "" + (icq));
				 }
				 if (picture == null) {
					 m.add("---picture---", "" + "ingen bild ännu");
				 } else {
					 m.add("---picture---", "" + (picture));
				 }
				 if (name == null) {
					 m.add("---name---", "" + "inget namn ännu");
				 } else {
					m.add("---name---", "" + (name));
				 }
				 m.add("---$username---", "" + (username));
   				 str = m.getMix();
   			 }
   		 } catch (Exception e) {
   			 e.printStackTrace();
   		 } finally {
   			 try {
   				 if (dbConnection != null) // Stänger databasanslutningen
   					 dbConnection.close();
   					 dbConnection = null;
   			 } catch (SQLException ignored) {
   				 ignored.printStackTrace();
   			 }
   		 }
   	 	 out.println(str);
   		 htmlTemplate = null;
	 }
}
