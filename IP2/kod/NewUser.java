/**
  * @NewUser.java 1.0 03/11/12
  *
  */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.CacheHttpServlet;

/**
  * Lägger in en ny användare i databasen samt skriver ut dennes egna sida.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Nerén
  */
public class NewUser extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private int number = 1;
	private String str;
	private String isUserExisting;
	private String username;

	String db_name = "db_03_carina_n";
	String db_username = "db_03_carina_n";
	String db_password = "691995";
	String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

	/**
	  * Hämtar värdena på specifika parametrar.
	  * Adderar dessa till databasen samt skriver ut gästboken.
	  */
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
		checkUserName(userName, passWord, out);
		if (isUserExisting.equals("true")) {
			userIsExisting(out, userName);
		} else {
			handleForm(userName, passWord, out);
	    	printMessages(out, userName);
	    	number++;
		}
  	}



  	/**
  	  * Kontrollerar om användarnamnet redan finns i databasen.
  	  */
  	private void checkUserName(String u, String p, PrintWriter o) {
		String uName = u;
		String pWord = p;
		PrintWriter out = o;
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
			stmt = dbConnection.createStatement();
			rs = stmt.executeQuery("SELECT username FROM user WHERE username=\"" + uName + "\"");

			if (rs.next()) {				//om inte username hittas skapas ingen rs o det finns ingen next.
				isUserExisting = "true";
			} else {
				isUserExisting = "false";
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
	}


	/** Användarnamnet fanns i databasen, skriver ut ett felmeddelande om det till användaren. */
	private void userIsExisting(PrintWriter out, String u) {
		String uName = u;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("NewUser.html")));
		}
		Mixer m = new Mixer(htmlTemplate);
		m.add("<!---entries--->", "DSV Forum Delux ser fram emot att just Du registrerar dig!", "" + ("Användaren " + uName + " finns redan, försök igen!"));
		str = m.getMix();
		out.println(str);
		htmlTemplate = null;
	}


  	/** Skriver ut en bekräftelse till användaren om att registreringen lyckades.*/
	private void printMessages(PrintWriter out, String userName) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("RegComplete.html")));
		}

		Mixer m = new Mixer(htmlTemplate);
		m.add("---username---", "" + (userName));
		str = m.getMix();
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


  	/** Adderar en användare och dess information till databasen. */
  	public void handleForm(String userName, String passWord, PrintWriter out) {
  	    Connection dbConnection = null;
  	    Statement stmt = null;
  	    try {
			  Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
			  dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
			  stmt = dbConnection.createStatement();
  	        stmt.executeUpdate("INSERT INTO user (username, password) VALUES (\"" + userName + "\",\"" + passWord + "\")");
  	        stmt.executeUpdate("INSERT INTO userGuestBook (username) VALUES (\"" + userName + "\")");
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