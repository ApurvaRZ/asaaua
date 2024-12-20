package com.cdac.mumbai.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClientIp {
	
	
	private String ip ;
	
	private String status;
	
	private Date whitlist_timestamp;
}
