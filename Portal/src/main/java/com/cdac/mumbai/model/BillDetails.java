package com.cdac.mumbai.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "pr_bill_details")
public class BillDetails implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private Integer bill_no;
	@Column(name = "reg_id")
	 private Integer regId;
   

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

	public BillDetails() {
		// TODO Auto-generated constructor stub
	}

	public BillDetails(Integer bill_no, Integer reg_id, String bill_year, String bill_quarter, Double bill_amount,
			Date bill_generation_date, Double surcharge, byte[] bill, String invoice_no, Date invoice_date,
			Double invoice_amount, String update_by, Timestamp update_timestamp) {
		super();
		this.bill_no = bill_no;
		this.regId = regId;
		this.bill_year = bill_year;
		this.bill_quarter = bill_quarter;
		this.bill_amount = bill_amount;
		this.bill_generation_date = bill_generation_date;
		this.surcharge = surcharge;
		this.bill = bill;
		this.invoice_no = invoice_no;
		this.invoice_date = invoice_date;
		this.invoice_amount = invoice_amount;
		this.update_by = update_by;
		this.update_timestamp = update_timestamp;
	}

	public Integer getReg_id() {
		return regId;
	}

	public void setReg_id(Integer reg_id) {
		this.regId = reg_id;
	}

	public Integer getBill_no() {
		return bill_no;
	}

	public void setBill_no(Integer bill_no) {
		this.bill_no = bill_no;
	}



	public String getBill_year() {
		return bill_year;
	}

	public void setBill_year(String bill_year) {
		this.bill_year = bill_year;
	}

	public String getBill_quarter() {
		return bill_quarter;
	}

	public void setBill_quarter(String bill_quarter) {
		this.bill_quarter = bill_quarter;
	}

	public Double getBill_amount() {
		return bill_amount;
	}

	public void setBill_amount(Double bill_amount) {
		this.bill_amount = bill_amount;
	}

	public Date getBill_generation_date() {
		return bill_generation_date;
	}

	public void setBill_generation_date(Date bill_generation_date) {
		this.bill_generation_date = bill_generation_date;
	}

	public Double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}

	public byte[] getBill() {
		return bill;
	}

	public void setBill(byte[] bill) {
		this.bill = bill;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public Date getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(Date invoice_date) {
		this.invoice_date = invoice_date;
	}

	public Double getInvoice_amount() {
		return invoice_amount;
	}

	public void setInvoice_amount(Double invoice_amount) {
		this.invoice_amount = invoice_amount;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Timestamp getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(Timestamp update_timestamp) {
		this.update_timestamp = update_timestamp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "BillDetails [bill_no=" + bill_no + ", reg_id=" + regId + ", bill_year=" + bill_year + ", bill_quarter="
				+ bill_quarter + ", bill_amount=" + bill_amount + ", bill_generation_date=" + bill_generation_date
				+ ", surcharge=" + surcharge + ", bill=" + Arrays.toString(bill) + ", invoice_no=" + invoice_no
				+ ", invoice_date=" + invoice_date + ", invoice_amount=" + invoice_amount + ", update_by=" + update_by
				+ ", update_timestamp=" + update_timestamp + "]";
	}

	

}
