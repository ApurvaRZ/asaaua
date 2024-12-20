package com.cdac.mumbai.service;

import java.util.List;

import com.cdac.mumbai.dto.ClientIp;



public interface IpService {
	
	String saveIp(String ip , String username);
	String deleteIpById(String ip);
	List<ClientIp> findByRegid(int regId);
	//boolean ipExists(String ip);
}
