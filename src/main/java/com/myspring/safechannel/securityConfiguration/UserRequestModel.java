package com.myspring.safechannel.securityConfiguration;

import java.util.List;



public class UserRequestModel {
	
	private String username;
	private String password;
	private List<String> role;
	
	
	public UserRequestModel(String username, String password, List<String> role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}
	public UserRequestModel() {
		super();
	}
	@Override
	public String toString() {
		return "UserRequestModel [username=" + username + ", password=" + password + ", role=" + role + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRole() {
		return role;
	}
	public void setRole(List<String> role) {
		this.role = role;
	} 
	
	

}
