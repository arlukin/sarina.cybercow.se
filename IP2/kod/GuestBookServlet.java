/*
 * @Ass05a.java 1.0 03/11/12
 *
 */

//package int6.servlet;

//import int6.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.naming.*;
import javax.servlet.*;
import javax.servlet.http.*;

//import com.oreilly.servlet.CacheHttpServlet;

/*
 * Använder en SQL-databas från ett HTML-formulär som skapar en gästbok.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class GuestBookServlet extends HttpServlet {
	private static GuestBook gb;

	//private int number = 1;
	//private String str;

	public void init() {
		gb = new GuestBook();
  	}


	/*
	 * Hämtar värdena på specifika parametrar.
	 * Adderar dessa till databasen samt skriver ut gästboken.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    //PrintWriter out = res.getWriter();

		/*String name = req.getParameter("name");
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

		Timestamp stamp = new Timestamp(new java.util.Date().getTime());
		String formatted = stamp.toString().substring(0,19);

		GbEntry entry = new GbEntry();
		entry.setG_Name(name);
        entry.setG_Email(email);
        entry.setG_HomePage(homepage);
        entry.setG_Comment(comment);
        entry.setG_Time(formatted);
		gb.addEntry(entry);*/
    	doGet(req, res);

  	}

  	/*
  	 * Skriver ut gästboken.
  	 */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setContentType("text/html");
	    //PrintWriter out = res.getWriter();

	    //printMessages(out);

	    List entries = gb.getEntries();
		req.setAttribute("entries", entries);
		RequestDispatcher rd = req.getRequestDispatcher("/visa.jsp");
    	rd.forward(req, res);
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

	public class GuestBook {
		String db_name = "Grupp_14";
		String db_username = "Grupp_14";
		String db_password = "okhbvnyr";
		String db_url = "java:comp/env/jdbc/";

		public List getEntries() {
			List entries = new ArrayList();
	    	//Connection con = null;
	    	//PreparedStatement pstat = null;

			Timestamp stamp = new Timestamp(new java.util.Date().getTime());
			String formatted = stamp.toString().substring(0,19);


	    	/*try {
				//con = ConnectionManager.getConnection();
	      		//pstat = con.prepareStatement("SELECT g_id, g_name, g_email, g_homepage, g_comment, g_time FROM guest ORDER BY g_id DESC");
	      		//ResultSet rs = pstat.executeQuery();

	      		while (rs.next()) {
					String id = rs.getString("g_id");
					String name = rs.getString("g_name");
					String email = rs.getString("g_email");
					String homepage = rs.getString("g_homepage");
					String comment = rs.getString("g_comment");
					String time = rs.getString("g_time");*/

	        		GbEntry entry = new GbEntry();
	        		entry.setG_Name("Anna");
	        		entry.setG_Email("email");
	        		entry.setG_HomePage("homepage");
	        		entry.setG_Comment("Vi är bäst!!!");
	        		entry.setG_Time("time");
	        		entries.add(entry);
	      		/*}
	      		//rs.close();
	    	} catch (Exception e) {

	    	} finally {
				try {
					pstat.close();
	      		} catch (SQLException e) {
					e.printStackTrace();
				}
	      		try {
					con.close();
	      		} catch (SQLException e) {
					e.printStackTrace();
				}
	    	}*/
	    	return entries;
	  	}


	  	public void addEntry(GbEntry entry) {
			Connection con = null;
	      	PreparedStatement pstmt =null;

	      	try {
				//con = ConnectionManager.getConnection();

				pstmt = con.prepareStatement("INSERT INTO guest (g_id, g_name, g_email, g_homepage, g_comment, g_time) VALUES(?,?,?,?,?,?)");
				pstmt.setString(1, entry.getG_Id());
				pstmt.setString(2, entry.getG_Name());
				pstmt.setString(3, entry.getG_Email());
				pstmt.setString(4, entry.getG_HomePage());
				pstmt.setString(5, entry.getG_Comment());
				pstmt.setString(6, entry.getG_Time());
				//stmt.setTimestamp(3,new Timestamp(entry.getTid().getTime()));
	      		pstmt.executeUpdate();

	    	} catch (SQLException e) {
				e.printStackTrace();

	    	} finally {
				try {
					pstmt.close();
	      		} catch (SQLException e) {
					e.printStackTrace();
					}
	      		try {
					con.close();
	      		} catch (SQLException e) {
					e.printStackTrace();
				}
	    	}
	  	}
	}



	public class GbEntry implements Serializable {
		private String g_id;
	  	private String g_name;
	  	private String g_email;
	  	private String g_homepage;
	  	private String g_comment;
	  	private String g_time;


	  	public String getG_Name() {
			return g_name;
	  	}


	  	public String getG_Id() {
	    	return g_id;
	  	}



	  	public String getG_Email() {
			return g_email;
		}



		public String getG_HomePage() {
			return g_homepage;
		}



		public String getG_Comment() {
			return g_comment;
		}



	  	public String getG_Time() {
		    return g_time;
	  	}



	  	public void setG_Name(String g_name) {
		   	this.g_name = g_name;
		}



	  	public void setG_Id(String g_id) {
	  	  	this.g_id = g_id;
	  	}



	  	public void setG_Email(String g_email) {
			this.g_email = g_email;
		}



		public void setG_HomePage(String g_homepage) {
			this.g_homepage = g_homepage;
		}



		public void setG_Comment(String g_comment) {
				this.g_comment = g_comment;
		}



	  	public void setG_Time(String g_time) {
	    	this.g_time = g_time;
  		}
	}



	/*public class ConnectionManager {
		private Connection c;

		public Connection getConnection() {
			try {
				Context context = new InitialContext();
	    		DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/");
				c = ds.getConnection();
	    	} catch (NamingException ne) {
				ne.printStackTrace();
			} catch (SQLException sqle) {
				sqle.printStackTrace();
			}
			return c;
	  	}
	}*/
}
