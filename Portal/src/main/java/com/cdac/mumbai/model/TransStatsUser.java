package com.cdac.mumbai.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;




@Entity
@Getter
@Setter
@ToString
@IdClass(TransStatsId.class)
@Table(name="vw_dept_wise_monthly_details_count")
public class TransStatsUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransStatsUser() {}
		
	@Id
	@Column(name="year_month")
	private String  year_month;
	
    @Id
	@Column(name="reg_id")
	private Integer  regId;
	 

	@Column(name="environment")
	private String environment ;
	
	@Transient
	private String count;
	//@Id
	@Column(name="trans_count")
	private Long  trans_count;
	
	@Column(name="transaction_type")
	private String  transaction_type;
	
	
	
	
	
	

	
	
	
	
}
