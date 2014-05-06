package de.dialogdata.aclabs.security;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.dialogdata.aclabs.entities.UserBE;
import de.dialogdata.aclabs.service.IUserService;
import de.dialogdata.aclabs.service.UserService;
import de.dialogdata.aclabs.utils.SecurityUtils;
import de.dialogdata.aclabs.utils.WebUtils;

@Named
@SessionScoped
public class LoginController implements Serializable{

	public static final String DD_LOGGED_USER = "dd.LoggedUser";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String password;
	
	@EJB
	private IUserService userService;
	
	public void logout(){
		
		this.setUser(null) ;
		
		FacesMessage msg = new FacesMessage("Logout succes!", "INFO MSG");
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        FacesContext.getCurrentInstance().addMessage(null, msg);
		
	}
	
	public String login(){
	
		System.out.println("Start login:" + userName + " - p:[" + password +"]");
		
		System.out.println("Start login:" + userName + " - p: " + SecurityUtils.encryptString(password));
		
		
		if(userService.validateCredentials(this.userName ,SecurityUtils.encryptString(this.password) )){
			
			System.out.println("Validation succes");
			
			this.setUser(userService.getByUsername(userName));
			FacesMessage msg = new FacesMessage("Login succes!", "INFO MSG");
	        msg.setSeverity(FacesMessage.SEVERITY_INFO);
	        FacesContext.getCurrentInstance().addMessage(null, msg);
			
			return "/index";
			
		}
		
		System.out.println("Validation fail");
		
		FacesMessage msg = new FacesMessage("Login error! User or password incorrect!", "ERROR MSG");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        FacesContext.getCurrentInstance().addMessage(null, msg);

		return "/login";
		
	}

	public String getUserName() {
		return userName;
	}

	public boolean isLoggedIn(){
		
		return( this.getUser() != null );
			
		
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public UserBE getUser() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		
		HttpSession session = (HttpSession) currentInstance.getExternalContext().getSession(true);
		
		Object storredUser =session.getAttribute(DD_LOGGED_USER);
		return storredUser == null ? null: (UserBE) storredUser;
	}
	
	public void setUser( UserBE user ){
		
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		
		HttpSession session = WebUtils.getSession((HttpServletRequest) currentInstance.getExternalContext().getRequest());
		session.setAttribute(DD_LOGGED_USER, user);
		
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
