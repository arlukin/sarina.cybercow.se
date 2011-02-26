/**
  * @FriendsList.java 1.0 03/11/12
  *
  */

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.CacheHttpServlet;

/**
  * Listar alla vänner/användare i databasen.
  *
  * @version 	1.0 12 November 2003
  * @author 	Carina Nerén
  */
public class FriendsList extends CacheHttpServlet {
	private static String htmlTemplate = null;
	private String str;

	private String db_name = "db_03_carina_n";
	private String db_username = "db_03_carina_n";
	private String db_password = "691995";
	private String db_url = "jdbc:mysql://atlas.dsv.su.se/db_03_carina_n";

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    PrintWriter out = res.getWriter();

	    printList(out);
	}


	/**
	  * Skriver ut en lista med alla användare i databasen.
	  */
	private void printList(PrintWriter out) {
		Connection dbConnection = null;
		Statement stmt = null;
		ResultSet rs = null;

		if(htmlTemplate == null) {
			htmlTemplate = Mixer.getContent(new File(getServletContext().getRealPath("FriendsList.html")));
		}

		try {
			Class.forName("org.gjt.mm.mysql.Driver").newInstance(); // Laddar JDBC-ODBC bryggan.
			dbConnection = DriverManager.getConnection(db_url, db_username, db_password); // Skapar en anslutning till databasen
			stmt = dbConnection.createStatement();
			rs = stmt.executeQuery("SELECT username FROM user");
			Mixer m = new Mixer(htmlTemplate);

			while (rs.next()) {
				String username = rs.getString("username");
				m.add("<!---entries--->", "---username---", "" + (username));
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
