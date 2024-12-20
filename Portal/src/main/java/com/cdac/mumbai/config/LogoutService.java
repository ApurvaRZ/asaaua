package com.cdac.mumbai.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.cdac.mumbai.common.SendMail;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		 {
	    final String authHeader = request.getHeader("Authorization");
	    final String jwt;
	    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
	      return;
	    }
	    jwt = authHeader.substring(7);
	   // var storedToken = authentication.//tokenRepository.findByToken(jwt)
	     //   .orElse(null);
	    if (jwt != null) {
	      
	      SecurityContextHolder.clearContext();
	    }
	}


}}