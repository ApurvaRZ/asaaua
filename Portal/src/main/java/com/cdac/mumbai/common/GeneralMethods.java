package com.cdac.mumbai.common;

import jakarta.servlet.http.HttpServletRequest;

public class GeneralMethods {
	
	public String getAppUrl(HttpServletRequest request) {
		if (request.getServerPort() == 8080 || request.getServerPort() == 80 || request.getServerPort() == 4002) {
			

			return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		}
		return "https://" + request.getServerName() + request.getContextPath();
	}

}
