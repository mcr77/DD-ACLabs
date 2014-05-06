package de.dialogdata.aclabs.test;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.service.IUserService;
import de.dialogdata.aclabs.utils.SecurityUtils;

	public class IServletContextListener implements ServletContextListener{
		 
		private static final String SYSTEM_USERNAME = "system";
		
		@EJB
		private IUserService userService;
		
		@Override
		public void contextDestroyed(ServletContextEvent arg0) {
			System.out.println("ServletContextListener destroyed");
		}
	 
		@Override
		public void contextInitialized(ServletContextEvent arg0) {
			if (userService.getByUsername(SYSTEM_USERNAME) == null){
			
			UserBE user = new UserBE();
			user.setUserName(SYSTEM_USERNAME);
			user.setPassword(SecurityUtils.encryptString(SYSTEM_USERNAME));;
			user.setStateAdmin(true);

			userService.createOrUpdate(user );
			
			}
			
			System.out.println("System started");	
		}
	}
