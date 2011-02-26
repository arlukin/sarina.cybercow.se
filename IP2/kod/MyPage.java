/**
  * @MyPage.java 1.0 03/11/12
  *
  */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.CacheHttpServlet;

/**
  * Användarens egna sida i forumet med personlig information.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Nerén
  */
public class MyPage extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private int number = 1;
	private String str;
	private String isLogginCorrect;

	private String db_name = "db_03_carina_n";
	private String db_username = "db_03_carina_n";
	private String db_password = "691995";
	private String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	/** Hämtar värdena på specifika parametrar. */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

		String userName = req.getParameter("username");
		if(userName.indexOf("<") != -1 && userName.indexOf(">") != -1) {
			userName = checkTags(userName);
		}
		String passWord = req.getParameter("password");
		if(passWord.indexOf("<") != -1 && passWord.indexOf(">") != -1) {
			passWord = checkTags(passWord);
		}
		checkLoggin(userName, passWord, out);
		if (isLogginCorrect.equals("userNametruepassWordtrue")) {
			printMessages(out, userName);
		}
		if (isLogginCorrect.equals("userNametruepassWordfalse")) {
			wrongPassword(out);
		}
		if (isLogginCorrect.equals("userNamefalsepassWordtrue")) {
			wrongUsername(out);
		}
		if (isLogginCorrect.equals("userNamefalsepassWordfalse")) {
			wrongPasswordandUsername(out);
		}
  	}



  	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();

		String userName2 = req.getParameter("username2");
		printMessages(out, userName2);
	}


	/**
	  * Kontrollerar om användaren skrivit in rätt användarnamn,
	  * samt lösenord.
	  */
	private void checkLoggin(String uName, String pWord, PrintWriter out) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		String uResultat;
		String pResultat;

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
			stmt = dbConnection.createStatement();
			rs = stmt.executeQuery("SELECT username FROM user WHERE username=\"" + uName + "\"");
			if (rs.next()) {				//om inte username hittas skapas ingen rs o det finns ingen next.
				uResultat = "userNametrue";
			} else {
				uResultat = "userNamefalse";
			}
			rs = null;

			rs = stmt.executeQuery("SELECT password FROM user WHERE password=\"" + pWord + "\"");
			if (rs.next()) {				//om inte username hittas skapas ingen rs o det finns ingen next.
				pResultat = "passWordtrue";
			} else {
				pResultat = "passWordfalse";
			}
			isLogginCorrect = uResultat.concat(pResultat);
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
	}


	/** Ger ett meddelande till användaren om att denna matat in fel lösenord. */
	private void wrongPassword(PrintWriter out) {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Loggin.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("<!---entries--->", "VÄLKOMMEN IN!!!", "" + ("FEL LÖSENORD, FÖRSÖK IGEN!"));
		str = m.getMix();
		out.println(str);
		htmlTemplate = null;
	}


	/** Ger ett meddelande till användaren om att denna matat in fel användarnamn. */
	private void wrongUsername(PrintWriter out) {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Loggin.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("<!---entries--->", "VÄLKOMMEN IN!!!", "" + ("FEL ANVÄNDARNAMN, FÖRSÖK IGEN!"));
		str = m.getMix();
		out.println(str);
		htmlTemplate = null;
	}


	/** Ger ett meddelande till användaren om att denna matat in fel lösenord och användarnamn. */
	private void wrongPasswordandUsername(PrintWriter out) {
		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("Loggin.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("<!---entries--->", "VÄLKOMMEN IN!!!", "" + ("FEL LÖSENORD OCH ANVÄNDARNAMN, FÖRSÖK IGEN!"));
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
		Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);

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


	/** Kollar om strängen innehåller HTML-taggar. */
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