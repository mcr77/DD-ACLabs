package de.dialogdata.aclabs.test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dialogdata.aclabs.security.LoginController;

public class SecurityFilter implements Filter{
	
	public void doFilter(ServletRequest request , ServletResponse response , FilterChain chain ) throws IOException, ServletException {
		LoginController loginController = (LoginController ) (( HttpServletRequest)request ).getSession().getAttribute("loginController");
		
		if( loginController == null || !loginController.isLoggedIn()){
			
			String contextPath = ((HttpServletRequest)request).getContextPath();
		
        ((HttpServletResponse)response).sendRedirect(contextPath + "/login.xhtml");
        
		}
		
		 chain.doFilter(request, response);
        
	}
	
	public void destroy() {

	}

	
	public void init(FilterConfig arg0) throws ServletException {
		
		
	}  
	
}
