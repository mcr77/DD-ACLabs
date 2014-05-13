package de.dialogdata.aclabs.security;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.service.IUserService;
import de.dialogdata.aclabs.service.UserService;
import de.dialogdata.aclabs.utils.SecurityUtils;

@Named
@SessionScoped
public class LoginController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	private UserBE user;
	
	@EJB
	private IUserService userService;
	
	public void logout(){
		
		user = null ;
		
		FacesMessage msg = new FacesMessage("Logout succes!", "INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public String login(){
	
		System.out.println("Start login");
		
		if(userService.validateCredentials(this.userName , this.password )){
			
			System.out.println("Validation succes");
			
			this.user = userService.getByUsername(userName);
			FacesMessage msg = new FacesMessage("Login succes!", "INFO MSG");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "/index";
			
		}
		
		System.out.println("Validation fail");
		
		FacesMessage msg = new FacesMessage("Login error!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);

		return "/error";
		
	}

	public String getUserName() {
		return userName;
	}

	public boolean isLoggedIn(){
		
		if( user == null )
			return false;
		else
			return true;
		
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public UserBE getUser() {
		return user;
	}
	
	public void setUser( UserBE user ){
		
		this.user = user;
		
	}

	public void setUserbyUsernameAndPassword(String userName , String password ) {
		
		SecurityUtils securityUtils = new SecurityUtils ();
		user.setPassword( securityUtils.encryptString( password ) );
		user.setUserName(userName);
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
