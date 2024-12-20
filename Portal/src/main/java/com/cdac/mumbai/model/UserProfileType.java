package com.cdac.mumbai.model;

public enum UserProfileType {
	USER("USER"),
	AUAUSER("AUAUSER"),
	ADMIN("ADMIN");
	
	String userProfileType;
	
	private UserProfileType(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
