package com.cdac.mumbai.model;


import java.util.Date;


import com.cdac.mumbai.validation.ValidIP;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
@IdClass(IpKey.class) 
@Table(name="aua_subaua_ips")
public class Ip {
	
//	@Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Transient
//    @Column(name = "id") // New unique primary key
//    private Long id;
	
	@Id
	@Column(name = "reg_id")
	 private Integer regId;
	
	@Column(name="status", unique=true, nullable=false)
	private String status;
	
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="whitlist_timestamp")
	private Date whitlist_timestamp;
    
    @Id
	@NotEmpty(message="IP address can not be null")
	@ValidIP(message = "Enter a valid IP address")
	@Column(name="ip", unique=true, nullable=false)
	private String ip ;


	public Integer getReg_id() {
		return regId;
	}


	public void setReg_id(Integer reg_id) {
		this.regId = reg_id;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public java.util.Date getWhitlist_timestamp() {
		return whitlist_timestamp;
	}


	public void setWhitlist_timestamp(java.util.Date whitlist_timestamp) {
		this.whitlist_timestamp = whitlist_timestamp;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((regId == null) ? 0 : regId.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((whitlist_timestamp == null) ? 0 : whitlist_timestamp.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ip other = (Ip) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (regId == null) {
			if (other.regId != null)
				return false;
		} else if (!regId.equals(other.regId))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (whitlist_timestamp == null) {
			if (other.whitlist_timestamp != null)
				return false;
		} else if (!whitlist_timestamp.equals(other.whitlist_timestamp))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "WhitelistingIp [reg_id=" + regId + ", status=" + status + ", whitlist_timestamp=" + whitlist_timestamp
				+ ", ip=" + ip + "]";
	}
	

}
