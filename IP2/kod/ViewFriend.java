/**
  * @ViewFriend.java 1.0 03/11/12
  *
  */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.CacheHttpServlet;

/**
  * Visar bes�karens v�ns egna sida med personlig information.
  * Denna sida kan inte bes�karen �ndra n�got i utan bara navigera.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Ner�n
  */
public class ViewFriend extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private int number = 1;
	private String str;
	private String friendUserName;

	private String db_name = "db_03_carina_n";
	private String db_username = "db_03_carina_n";
	private String db_password = "691995";
	private String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    printMessages(out, friendUserName);
  	}


  	/** H�mtar v�rdet fr�n den specifika paramatern samt anropar doPost. */
  	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    friendUserName = req.getParameter("guestUsername");
	    doPost(req, res);
	}


  	/**
	  * Skapar en anslutning till en databas och h�mtar information om
	  * anv�ndaren samt skriver ut den personliga sidan.
  	  */
	private void printMessages(PrintWriter out, String userName) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("MyFriendsPage.html")));
		}
		Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);


		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
			stmt = dbConnection.createStatement();
			rs = stmt.executeQuery("SELECT username, password, email, homepage, presentation, icq, picture, name FROM user WHERE username=\"" + userName + "\"");
			Mixer m = new Mixer(htmlTemplate);

			while (rs.next()) {
				String username = rs.getString("username");
				String password = rs.getString("password");
				String email = rs.getString("email");
				String homepage = rs.getString("homepage");
				String presentation = rs.getString("presentation");
				String icq = rs.getString("icq");
				String picture = rs.getString("picture");
				String name = rs.getString("name");
				m.add("---username---", "" + (userName));
				m.add("---$friendusername---", "" + (friendUserName));
				if (email == null) {
					m.add("---email---", "" + "ingen email �nnu");
				} else {
					m.add("---email---", "" + (email));
				}
				if (homepage == null) {
					m.add("---homepage---", "" + "ingen hemsida �nnu");
				} else {
					m.add("---homepage---", "" + (homepage));
				}
				if (presentation == null) {
					m.add("---presentation---", "" + "ingen presentation �nnu");
				} else {
					m.add("---presentation---", "" + (presentation));
				}
				if (icq == null) {
					m.add("---icq---", "" + "ingen icq �nnu");
				} else {
					m.add("---icq---", "" + (icq));
				}
				if (picture == null) {
					m.add("---picture---", "" + "ingen bild �nnu");
				} else {
					m.add("---picture---", "" + (picture));
				}
				if (name == null) {
					m.add("---name---", "" + "inget namn �nnu");
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
				if (dbConnection != null) // St�nger databasanslutningen
					dbConnection.close();
					dbConnection = null;
			} catch (SQLException ignored) {
				ignored.printStackTrace();
			}
		}
		out.println(str);
		htmlTemplate = null;
	}


	/** Kollar om str�ngen inneh�ller HTML-taggar. */
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
}