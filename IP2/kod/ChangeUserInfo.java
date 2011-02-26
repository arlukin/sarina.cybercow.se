/*
 * @ChangeUserInfo.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.oreilly.servlet.CacheHttpServlet;

/*
 * �ndrar anv�ndarens information.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class ChangeUserInfo extends CacheHttpServlet {
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
	 * H�mtar v�rdena p� specifika parametrar.
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
		String icq = req.getParameter("icq");
		if(icq.indexOf("<") != -1 && icq.indexOf(">") != -1) {
			icq = checkTags(icq);
		}
		String homepage = req.getParameter("homepage");
		if(homepage.indexOf("<") != -1 && homepage.indexOf(">") != -1) {
			homepage = checkTags(homepage);
		}
		handleForm(userName, name, email, icq, homepage, out);
	    printMessages(out, userName);
  	}


	/** H�mtar v�rdet p� den specifika parametern. */
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
  	 * Skriver ut formul�ret d�r anv�nderen kan �ndra sin information om sig sj�lv.
  	 */
	private void printForm(PrintWriter out, String userName) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("ChangeUserInfo.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		str = m.getMix();
		out.println(str);
		htmlTemplate = null;
	}


	/**
	  * Skapar en anslutning till en databas och h�mtar information om
	  * anv�ndaren.
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


	/* Kollar om str�ngen inneh�ller HTML-taggar. */
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
 	  * Uppdaterar databasen med anv�ndarens information.
 	  */
  	public void handleForm(String username, String name, String email, String icq, String homepage, PrintWriter out) {
		Connection dbConnection = null;
      	Statement stmt = null;

      	try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
		  	dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
		  	stmt = dbConnection.createStatement();
		  	stmt.executeUpdate("UPDATE user SET name=\"" + name + "\", email=\"" + email + "\", icq=\"" + icq + "\", homepage=\"" + homepage + "\" WHERE username=\"" + username + "\"");
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
