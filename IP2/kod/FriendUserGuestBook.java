/*
 * @FriendUserGuestBook.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.CacheHttpServlet;

/*
 * En personlig gästbok till besökarens vän/användare.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class FriendUserGuestBook extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private static String htmlTemplate2 = null;
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
	 * Adderar dessa till databasen samt skriver ut gästboken.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

		String guestUserName = req.getParameter("guestusername");
		if(guestUserName.indexOf("<") != -1 && guestUserName.indexOf(">") != -1) {
			guestUserName = checkTags(guestUserName);
		}
		String comment = req.getParameter("comment");
		if(comment.indexOf("<") != -1 && comment.indexOf(">") != -1) {
			comment = checkTags(comment);
		}
		handleForm(guestUserName, comment, userName);
	    printMessages(out, userName);
  	}

  	/*
  	 * Hämtar värden på en specifik parameter.
  	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    userName = req.getParameter("friendusername");
		if(userName.indexOf("<") != -1 && userName.indexOf(">") != -1) {
			userName = checkTags(userName);
		}

	    printMessages(out, userName);
	}


  	/*
  	 * Lägger till ett meddelande och övrig information till gästboken.
  	 */
	private void printMessages(PrintWriter out, String userName) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("FriendUserGuestBook.html")));
		}
		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan som är en specifik driver.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen. DriverManager frågar varje registrerad driver ifall den känner igen URL:en och om den gör det använder driver manager den drivern för att skapa en Connection.
			stmt = dbConnection.createStatement(); // Skapar ett meddelande som används vid utförandet av en förfrågan.
			rs = stmt.executeQuery("SELECT guestusername, comment, username, time FROM userGuestBook WHERE username=\"" + userName + "\""); // Ett objekt som är en representation av resultatet i frågan returnerat en rad i taget.
			Mixer m = new Mixer(htmlTemplate);

			while (rs.next()) {
				String gUserName = rs.getString("guestusername");
				String comment = rs.getString("comment");
				String time = rs.getString("time");
				m.add("---userName---", "" + (userName));
				if (gUserName == null) {
					m.add("<!---entries--->", "---guestUserName---", "" + ("ingen"));
				} else {
					m.add("<!---entries--->", "---guestUserName---", "" + (gUserName));
				}
				if (comment == null) {
					m.add("<!---entries--->", "---comment---", "" + ("ingen kommentar"));
				} else {
					m.add("<!---entries--->", "---comment---", "" + (comment));
				}
				if (time == null) {
					m.add("<!---entries--->", "---time---", "" + ("ingen tid"));
				} else {
					m.add("<!---entries--->", "---time---", "" + (time));
				}
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
 	  * Adderar guestusername, comment, username samt time till databasen. Har
 	  * just dessa strängar som parametrar.
 	  */
  	public void handleForm(String guestUserName, String comment, String userName) {
		Connection dbConnection = null;
      	Statement stmt = null;
      	Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);
      	try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
		  	dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
		  	stmt = dbConnection.createStatement();
          	stmt.executeUpdate("INSERT INTO userGuestBook (guestusername, comment, username, time) VALUES (\"" + guestUserName + "\",\"" + comment + "\", \"" + userName + "\", \"" + formatted + "\")");
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
