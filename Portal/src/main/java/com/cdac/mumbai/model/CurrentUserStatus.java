package com.cdac.mumbai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pr_m_user_account_status")
public class CurrentUserStatus {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name=" status_code ", unique=true, nullable=false)
	private String  statusCode;

	@Column(name="status", unique=true, nullable=false)
	private String  status;

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	
	
}
