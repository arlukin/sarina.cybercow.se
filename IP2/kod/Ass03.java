/*
 * @Ass03.java 1.0 03/11/12
 *
 */

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;
import java.io.OutputStream.*;

/*
 * Laddar upp filer fr�n klienten till servern.
 *
 * @version 	1.0 12 November 2003
 * @author 	Carina Ner�n
 */
public class Ass03 extends HttpServlet {
  	private File f;
  	private long length;
  	private String type;
  	private String fileName;

	/*
	 * Skapar ett multirequest f�r att underl�tta l�sning av informationen i filen.
	 * S�tter �vre gr�nsen f�r hur mycket information som ska tas emot till 5G.
	 * Filnamn, filtyp samt filstorlek h�mtas, d�refter kontrolleras filtypen och
	 * beroende p� vilken det �r skrivs antingen inneh�llet i den ut eller enbart
	 * dess filnamn, filtyp samt filstorlek.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	try {
			MultipartRequest multi = new MultipartRequest(request, ".", 5 * 1024 * 1024);

			Enumeration files = multi.getFileNames();
			while (files.hasMoreElements()) {
				String name = (String)files.nextElement();
			    fileName = multi.getFilesystemName(name);
			    //String original = multi.getOriginalFileName(name);
			    type = multi.getContentType(name);
			    f = multi.getFile(name);
			}
		 }
		 catch (Exception e) {
			 //e.printStackTrace(out);
		 }
		 if (type.equals("text/plain") || type.equals("image/gif") || type.equals("image/pjpeg") || type.equals("image/jpeg") || type.equals("image/x-png")) {
			 ServletOutputStream servletOut = response.getOutputStream();
			 response.setContentType(type);
			 BufferedInputStream bri = null;
			 synchronized(this) {
				 try {
					 bri = new BufferedInputStream(new FileInputStream(f));
					 int l;
					 byte[] buffer = new byte[2000];
					 while ((l = bri.read(buffer, 0, 2000)) != -1) {
						 servletOut.write(buffer, 0, l);
					 }
				 } catch(FileNotFoundException fnfe) {
					 System.err.println("Kunde inte hitta " + f);
				 } catch(IOException ioe) {
					 System.err.println("Problem vid l�sningen av " + f);
				 } catch(NumberFormatException nfe) {
					 System.err.println("Ej fungerande v�rde i " + f);
				 } finally {
					 try {
						 if(bri != null) {
							 bri.close();
						 }
					 } catch(IOException ioe) {
						 System.err.println("Problem vid st�ngningen av BufferedInputStream");
					 }
				 }
	   		 }
		 } else {
			 PrintWriter out = response.getWriter();
		   	 out.println("Filnnamn: " + fileName);
		   	 out.println("Filtyp: " + type);
		   	 out.println("Filstorlek: " + f.length() + "kB");
		 }
	 }
 }









