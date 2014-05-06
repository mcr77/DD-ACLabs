package de.dialogdata.aclabs.servletlistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class AServletContextListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		// Drob database content
		System.out.println("\n\n\n\n");
		System.out
				.println("Undeploy database ###########################################");
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		// Initialize database
		System.out.println("\n\n\n\n");
		System.out
				.println("Creating database ###########################################");
	}

}
