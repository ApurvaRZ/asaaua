package com.cdac.mumbai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="pr_m_state")
public class Mstate {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="state_id", unique=true, nullable=false)
	private String  state_id;

	@Column(name="state_name", unique=true, nullable=false)
	private String  state_name;

	public String getState_id() {
		return state_id;
	}

	public void setState_id(String state_id) {
		this.state_id = state_id;
	}

	public String getState_name() {
		return state_name;
	}

	public void setState_name(String state_name) {
		this.state_name = state_name;
	}

	
	
}
