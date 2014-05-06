package de.dialogdata.aclabs.test;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public final class IFilter implements Filter {
	private FilterConfig filterConfig = null;

	public void init(FilterConfig config) throws ServletException {

		String name = config.getInitParameter("Ivett");

		// Print the init parameter
		System.out.println(name);
	}

	public void destroy() {
		this.filterConfig = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws java.io.IOException, ServletException {
		
		String name = request.getParameter("name");
		
		// Print the init parameter
		System.out.println(name);
		
		// Pass request back down the filter chain
		chain.doFilter(request, response);
	}
}
