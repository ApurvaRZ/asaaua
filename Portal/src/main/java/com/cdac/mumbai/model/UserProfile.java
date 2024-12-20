package com.cdac.mumbai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="PR_CLIENT_ROLES")
@Data
public class UserProfile {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "role_id")
	
	private int roleId;	

	@Column(name="role_name", length=15, unique=true, nullable=false)
	private String type = UserProfileType.USER.getUserProfileType();
}
