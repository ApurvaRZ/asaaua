package com.cdac.mumbai.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
	 private String token;
	    private long expiresAt;

	    public AuthenticationResponse(String token, long expiresAt) {
	        this.token = token;
	        this.expiresAt = expiresAt;
	    }
}
