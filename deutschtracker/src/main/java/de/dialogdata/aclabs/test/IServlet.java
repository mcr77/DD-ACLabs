package de.dialogdata.aclabs.test;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGe(HttpServletRequest request, HttpServletResponse response)
			throws IOException{
				PrintWriter out = response.getWriter();
				out.println("<html>");
				out.println("<body>");
				out.println("<h1>Name</h1>");
				out.println("</body>");
				out.println("</html>");	
			}
}
