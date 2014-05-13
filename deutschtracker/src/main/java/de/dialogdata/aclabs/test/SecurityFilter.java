package de.dialogdata.aclabs.test;

import java.io.IOException;

import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.dialogdata.aclabs.security.LoginController;

public class SecurityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		System.out.println("\n\nSecurity Check\n\n");

		
		String contextPath = ((HttpServletRequest) request).getContextPath();

		if (!(((HttpServletRequest) request).getRequestURL().toString()
				.endsWith("login.xhtml"))) {

		
			HttpSession session= ((HttpServletRequest) request).getSession(false);
			
			if ( session == null || session.getAttribute(LoginController.DD_LOGGED_USER) == null) {

				((HttpServletResponse) response).sendRedirect(contextPath
						+ "/faces/login.xhtml");

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
