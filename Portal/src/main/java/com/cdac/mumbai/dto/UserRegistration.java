package com.cdac.mumbai.dto;

import lombok.Data;

@Data
public class UserRegistration {
	private String organizationName;
	private String address;
	private String  organizationType;
	private String dept;//dept name required in case of government Organization only
	private String ministry;//ministry name of government Organization
	private String registartion_no; // registartion number of private Organization
	private String  city;
	private String state;
	private String pincode;
	private String  phone;  
	private String service_type;
	private String aua_kua_code;// preproduction code
	private String aua_kua_code_prod; //production code
	private String is_alternate_sp ;// alernate service provider status
	private String alternate_sp_name ;// alernate service provider name
	private String  cd_name;// name of contact person
	private String cd_designation;//designation of contact person
	private String  cdemail;// email of contact person
	private String  cd_mobile;//mobile no. 
	private Integer  quarterly_transaction_count;// no of quarterly transaction count
	private String  quarterly_transaction_in_words;
	private String project_description;
	private String project_purpose;
	
}
