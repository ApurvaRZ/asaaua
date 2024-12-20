package com.cdac.mumbai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.*;


@Entity
@Getter
@Setter
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY,region="saconfigpara")
@Table(name="sa_config_para")
public class SaConfigPara {
	
	@Id
	@Column(name="para_name", unique=true, nullable=false)
	private String paraName;
  
	@Column(name="para_value", unique=true, nullable=false)
	private String paraValue;

	@Column(name="para_byte_values", unique=true)
	private byte[] paraByteValues;
	

	

}
