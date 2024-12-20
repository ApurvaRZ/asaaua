package com.cdac.mumbai.dto;

import java.sql.Date;
import java.sql.Timestamp;


import lombok.Data;

@Data
public class CostSummary {
	
	private Integer bill_no;

//    private Integer reg_id;

	private String bill_year;
	private String bill_quarter;
	private Double bill_amount;
	private Date bill_generation_date;
	

	private Double surcharge;
	private byte[] bill;
	private String invoice_no;
	private Date invoice_date;
	private Double invoice_amount;
	private String update_by;
	private Timestamp update_timestamp;
}
