package de.dialogdata.aclabs.security;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dialogdata.aclabs.utils.WebUtils;

public class SecurityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		System.err.println("\n\nSecurity Check\n\n");

		
		String contextPath = ((HttpServletRequest) request).getContextPath();

		String requestURL = ((HttpServletRequest) request).getRequestURL().toString();
		
		System.err.println("security url check: "+requestURL);
		if (requestURL.toLowerCase().endsWith(".xhtml") 
				&& !requestURL.toLowerCase().endsWith("login.xhtml")) {
		
			HttpSession session= WebUtils.getSession((HttpServletRequest) request);
			
			if ( session == null || session.getAttribute(LoginController.DD_LOGGED_USER) == null) {
				
				System.err.println("Redirect login: "+requestURL);

				((HttpServletResponse) response).sendRedirect(contextPath
						+ "/faces/login.xhtml");
				return;
			}
		}
		

		chain.doFilter(request, response);

	}
    
    private static abstract class FacesContextWrapper extends FacesContext {
        protected static void setCurrentInstance(FacesContext facesContext) {
            FacesContext.setCurrentInstance(facesContext);
        }
    }     
    
	
	public void destroy() {

	}

	public void init(FilterConfig arg0) throws ServletException {

		System.out.println("\n\nSecurity Filter is stasting!\n\n");

	}

}
