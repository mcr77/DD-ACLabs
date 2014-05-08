package de.dialogdata.aclabs.security;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import de.dialogdata.aclabs.entities.UserBE;

@Named
@SessionScoped
public class LoginController implements Serializable{

	
	private String userName;
	private String password;
	private UserBE user;
	
	public void logout(){
		
		user = null ;
		
	}
	
	public void login(){
		
		
		
	}
	
}
