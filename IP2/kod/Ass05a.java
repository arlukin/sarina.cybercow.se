/*
 * @Ass05a.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.CacheHttpServlet;

/*
 * Använder en SQL-databas från ett HTML-formulär som skapar en gästbok.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass05a extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private static String htmlTemplate2 = null;
	private static int number = 1;
	private static String str;

	private static String db_name = "db_03_carina_n";
	private static String db_username = "db_03_carina_n";
	private static String db_password = "691995";
	private static String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	/*
	 * Hämtar värdena på specifika parametrar.
	 * Adderar dessa till databasen samt skriver ut gästboken.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

		String name = req.getParameter("name");
		if(name.indexOf("<") != -1 && name.indexOf(">") != -1) {
			name = checkTags(name);
		}
		String email = req.getParameter("email");
		if(email.indexOf("<") != -1 && email.indexOf(">") != -1) {
			email = checkTags(email);
		}
		String homepage = req.getParameter("homepage");
		if(homepage.indexOf("<") != -1 && homepage.indexOf(">") != -1) {
			homepage = checkTags(homepage);
		}
		String comment = req.getParameter("comment");
		if(comment.indexOf("<") != -1 && comment.indexOf(">") != -1) {
			comment = checkTags(comment);
		}

		handleForm(name, email, homepage, comment);
	    printMessages(out);
  	}

  	/*
  	 * Skriver ut gästboken.
  	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    printMessages(out);
	}


  	/*
  	 * Skapar en anslutning till en databas till vilken en fråga utförs där resultatet blir
  	 * användarens id, namn, email, hemsida samt kommentar. Resultatet
  	 * på frågan adderas sedan till den hämtade HTML-filen som skickas till klienten.
  	 */
	private void printMessages(PrintWriter out) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Ass05a.html")));
		}
		Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan som är en specifik driver.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen. DriverManager frågar varje registrerad driver ifall den känner igen URL:en och om den gör det använder driver manager den drivern för att skapa en Connection.
			stmt = dbConnection.createStatement(); // Skapar ett meddelande som används vid utförandet av en förfrågan.
			rs = stmt.executeQuery("SELECT g_id, g_name, g_email, g_homepage, g_comment, g_time FROM guest ORDER BY g_id DESC"); // Ett objekt som är en representation av resultatet i frågan returnerat en rad i taget.
			Mixer m = new Mixer(htmlTemplate);

			while (rs.next()) {
				String id = rs.getString("g_id");
				String name = rs.getString("g_name");
				String email = rs.getString("g_email");
				String homepage = rs.getString("g_homepage");
				String comment = rs.getString("g_comment");
				String time = rs.getString("g_time");
				m.add("<!---entries--->", "---no---", "" + (id));
				m.add("<!---entries--->", "---time---", "" + (time));
				m.add("<!---entries--->", "---name---", "" + (name));
				m.add("<!---entries--->", "---email---", "" + (email));
				m.add("<!---entries--->", "---homepage---", "" + (homepage));
				m.add("<!---entries--->", "---comment", "" + (comment));
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
		number++;
		out.println(str);
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
 	  * Adderar name, email, homepage samt comment till databasen. Har
 	  * just dessa strängar som parametrar.
 	  */
  	public void handleForm(String name, String email, String homepage, String comment) {
		Connection dbConnection = null;
      	Statement stmt = null;
      	Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);
      	try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
		  	dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
		  	stmt = dbConnection.createStatement();
          	stmt.executeUpdate("INSERT INTO guest (g_id, g_name, g_email, g_homepage, g_comment, g_time) VALUES (\"" + number + "\",\"" + name + "\",\"" + email + "\",\"" + homepage + "\",\"" + comment + "\", \"" + formatted + "\")");
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
