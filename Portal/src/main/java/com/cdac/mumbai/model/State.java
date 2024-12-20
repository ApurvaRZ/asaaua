package com.cdac.mumbai.model;






import lombok.Getter;

import lombok.ToString;


@Getter
@ToString
public enum State {
	ACTIVE("Active"),
	INACTIVE("NewRequest"),
	DELETED("Deleted"),
	LOCKED("Locked"),
	External("E");


	private String state;
	
    private State(final String state){
 		this.state = state;
	}
    
    public String getName() {
        return this.name();
    }

	String getState() {
		
		return null;
	}
}
