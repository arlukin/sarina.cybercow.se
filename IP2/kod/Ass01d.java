/*
 * @Ass01d.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*
 * Sänder en dynamisk mängd data till användaren.
 * Denna mängd visar visar samtliga omgivningsvaribler.
 * Värdet sänds som HTML till användaren.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Nerén
 */
public class Ass01d extends HttpServlet {
	private static String htmlTemplate = null;

	/*
	 * Laddar den HTML-fil som omgivningsvariablerna ska visas på
	 * samt returnerar innehållet som en sträng.
	 */
	public void init() throws ServletException {
		if(htmlTemplate==null){
			htmlTemplate=Mixer.getContent(new File(getServletContext().getRealPath("Ass01d.html")));
		}
		BufferedReader br = null;
	}


  	/* Anropar doGet. */
  	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
  		doGet(request, response);
    }

    /*
     * Hämtar alla olika omgivningsvariabler samt skriver
	 * ut namnet och dess värde till en HTML-fil som sänds
	 * till användaren.
   	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
  	  	response.setContentType("text/html");
  	  	PrintWriter out = response.getWriter();
      	ServletContext sc = getServletContext(); // Erbjuder information om serverns mjukvara och dess attribut.
  	  	Enumeration e = null;					 // En uppräkning av strängobjekt.


  	  	// SERVER DELEN.

  	  	// Servlet namn.
  	  	Mixer m = new Mixer(htmlTemplate);
	  	String servletname = getServletName();
	  	m.add("---servletname---", servletname);

	  	// Servlet init parameters.
	  	e = getInitParameterNames();
	  	while (e.hasMoreElements()) {
			String key = (String)e.nextElement();
  	  	    String value = getInitParameter(key);
  	  	    m.add("<!---aaa--->", "---servletinitparam---", key + " ::: " + value);
	  	}

	  	// Servlet context init parameters.
	  	e = sc.getInitParameterNames();
	  	while (e.hasMoreElements()) {
	  	    String key = (String)e.nextElement();
  	  	    String value = sc.getInitParameter(key);
  	  	    m.add("<!---aaa--->", "---servletcontextparam---", key + " ::: " + value);
	  	}

	  	// Servlet namn, port samt info.
	  	String servername = request.getServerName();
	  	m.add("---servername---", servername);
	  	String serverport = Integer.toString (request.getServerPort());
	  	m.add("---serverport---", serverport);
	  	String serverinfo = sc.getServerInfo();
	  	m.add("---serverinfo---", serverinfo);

	  	// Server attribut.
	  	e = sc.getAttributeNames();
	  	while (e.hasMoreElements()) {
	  	    String key = (String)e.nextElement();
  	  	    Object value = sc.getAttribute(key);
  	  	    if (value.getClass().isArray()) {
				Object[] arr = (Object[]) value;
			    StringBuffer sb = new StringBuffer();
			    List l = Arrays.asList(arr);
			    Iterator it = l.iterator();
			    while (it.hasNext()) {
					sb.append(it.next().toString()).append(' ');
				}
				m.add("<!---bbb--->", "---serverattributes---", key + " ::: " + sb);
			}
			m.add("<!---bbb--->", "---serverattributes---", key + " ::: " + value);
	  	}

	  	// Server versions.
	  	String majorversion = Integer.toString(sc.getMajorVersion());
	  	m.add("---majorversion---", majorversion);
	  	String minorversion = Integer.toString(sc.getMinorVersion());
	  	m.add("---minorversion---", minorversion);
	  	String jdkversion = System.getProperty("java.version");
	  	m.add("---jdkversion---", jdkversion);


	  	// CLIENTDELEN.

	  	// Client adress, värd, användare samt typ.
  	  	String remoteAddress = request.getRemoteAddr();
	  	m.add("---remoteAddress---", remoteAddress);
	  	String remoteHost = request.getRemoteHost();
	  	m.add("---remoteHost---", remoteHost);
	  	String remoteUser = request.getRemoteUser();
	  	m.add("---remoteUser---", remoteUser);
	  	String authenticType = request.getAuthType();
	  	m.add("---AuthenticType---", authenticType);

  	  	// Client headers.
  	  	e = request.getHeaderNames();
	  	while (e.hasMoreElements()) {
	  	    String key = (String)e.nextElement();
  	  	    String value = request.getHeader(key);
  	  	    m.add("<!---ccc--->", "---headernames---", key + " ::: " + value);
	  	}

	  	// Client Request parameters.
	  	String queryString = request.getQueryString();
	  	m.add("---queryString---", queryString);
	  	String charactarEncoding = request.getCharacterEncoding();
	  	m.add("---charactarEncoding---" , charactarEncoding);
	  	String contentLength = Integer.toString(request.getContentLength());
	  	m.add("---contentLength---", contentLength);
	  	String contentType = request.getContentType();
	  	m.add("---contentType---", contentType);

	  	// Client requestparameters, one by one.
	  	e = request.getParameterNames();
	  	while (e.hasMoreElements()) {
	  	    String key = (String)e.nextElement();
	  	    String[] values = request.getParameterValues(key);
  	  	    for(int i = 0; i < values.length; i++) {
				if (values.getClass().isArray()) {
					Object[] arr = (Object[]) values;
				  	StringBuffer sb = new StringBuffer();
				  	List l = Arrays.asList(arr);
				  	Iterator it = l.iterator();
				  	while (it.hasNext()) {
						sb.append(it.next().toString()).append(' ');
				  	}
				  	m.add("<!---ddd--->", "---parameterNames---", key + " ::: " + sb);
				}
				m.add("<!---ddd--->", "---parameterNames---", key + " ::: " + values);
			}
	  	}

	  	// Client path and connection information.
	  	String pathInformation = request.getPathInfo();
	  	m.add("---pathInformation---", pathInformation);
	  	String pathTranslated = request.getPathTranslated();
	  	m.add("---pathTranslated---", pathTranslated);
	  	String uri = request.getRequestURI();
	  	m.add("---uri---", uri);
	  	String url = request.getRequestURL().toString();
	  	m.add("---url---", url);
	  	String scheme = request.getScheme();
	  	m.add("---scheme---", scheme);
	  	String contextPath = request.getContextPath();
	  	m.add("---contextPath---", contextPath);
	  	String servletPath = request.getServletPath();
	  	m.add("---servletPath---", servletPath);
	  	String protocol = request.getProtocol();
	  	m.add("---protocol---", protocol);
	  	String method = request.getMethod();
	  	m.add("---method---", method);

	  	// Client request attributes.
	  	e = request.getAttributeNames();
	  	while (e.hasMoreElements()) {
	  	    String key = (String)e.nextElement();
  	  	    Object value = request.getAttribute(key);
  	  	    if (value.getClass().isArray()) {
				Object[] arr = (Object[]) value;
			  	StringBuffer sb = new StringBuffer();
			    List l = Arrays.asList(arr);
			  	Iterator it = l.iterator();
			  	while (it.hasNext()) {
					sb.append(it.next().toString()).append(' ');
			  	}
			  	m.add("<!---eee--->", "---clientAttribute---", key + " ::: " + sb);
			}
			m.add("<!---eee--->", "---clientAttribute---", key + " ::: " + value);
	  	}
  		out.println(m.getMix());
	}
 }


