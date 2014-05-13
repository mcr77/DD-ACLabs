package de.dialogdata.aclabs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AFilter implements Filter {

	private FilterConfig filterConfig;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		//System.out.println("\n\n\n");
		//System.out
				//.println("AFilter is doing something  ################################");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

		// Message that filter starts
		//System.out.println("\n\n\n");
		//System.out
				//.println("Init filter -----------------------------------------------");
		this.filterConfig = filterConfig;

	}

	@Override
	public void destroy() {

		// Message that filter is destroyed
		//System.out.println("\n\n\n");
		//System.out
			//	.println("Destroy filter --------------------------------------------");
		this.filterConfig = null;

	}

}
