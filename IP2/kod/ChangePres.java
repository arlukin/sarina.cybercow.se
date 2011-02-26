/*
 * @ChangePres.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.CacheHttpServlet;

/*
 * Ändrar användarens presentation.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class ChangePres extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private int number = 1;
	private String str;
	private String userId;
	private String userName;

	private String db_name = "db_03_carina_n";
	private String db_username = "db_03_carina_n";
	private String db_password = "691995";
	private String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	/*
	 * Hämtar värdena på specifika parametrar.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

		String presentation = req.getParameter("text_area");
		if(presentation.indexOf("<") != -1 && presentation.indexOf(">") != -1) {
			presentation = checkTags(presentation);
		}
		handleForm(presentation, userName, out);
	    printMessages(out, userName);
  	}


	/** Hämtar värdet på den specifika parametern. */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    userName = req.getParameter("username");
		if(userName.indexOf("<") != -1 && userName.indexOf(">") != -1) {
			userName = checkTags(userName);
		}
	    printForm(out, userName);
	}


  	/*
  	 * Skriver ut formuläret där användaren kan ändra sin presentation.
  	 */
	private void printForm(PrintWriter out, String userName) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("ChangePres.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		str = m.getMix();
		out.println(str);
		htmlTemplate = null;
	}


	/**
	  * Skapar en anslutning till en databas och hämtar information om
	  * användaren.
  	  */
	private void printMessages(PrintWriter out, String userName) {
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
			rs = stmt.executeQuery("SELECT username, password, email, homepage, presentation, icq, picture, name FROM user WHERE username=\"" + userName + "\"");
			Mixer m = new Mixer(htmlTemplate);

			while (rs.next()) {
				String password = rs.getString("password");
				String email = rs.getString("email");
				String homepage = rs.getString("homepage");
				String presentation = rs.getString("presentation");
				String icq = rs.getString("icq");
				String picture = rs.getString("picture");
				String name = rs.getString("name");
				m.add("---username---", "" + (userName));
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
				m.add("---$username---", "" + (userName));
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


	/* Kollar om strängen innehåller HTML-taggar. */
	public String checkTags(String str) {
		String ny = "";
		String[] strings = str.split(">");
		for (int x=0; x<strings.length; x++) {
			if(strings[x].indexOf("<") != -1) {
				int i = strings[x].indexOf("<");
				String sub = strings[x].substring(i);
				String rep = strings[x].replaceAll(sub, "");
				ny = ny.concat(rep);
			} else {
				ny = ny.concat(strings[x]);
			}
		}
		str = ny;
		return str;
	}


 	 /*
 	  * Uppdaterar databasen med användarens presentation.
 	  */
  	public void handleForm(String presentation, String username, PrintWriter out) {
		Connection dbConnection = null;
      	Statement stmt = null;

      	try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
		  	dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
		  	stmt = dbConnection.createStatement();
		  	stmt.executeUpdate("UPDATE user SET presentation=\"" + presentation + "\" WHERE username=\"" + username + "\"");
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
}
