package com.cdac.mumbai.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pr_agreement")
@Getter
@Setter
public class Pr_Agreement implements Serializable {

	private static final long serialVersionUID = 5208188709890269455L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agreement_id", columnDefinition = "serial")
	private int agreement_id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "reg_id", nullable = false)
	private User auasubaua;
	private boolean is_agreement_sent;
	private Date agreement_sent_date;
	private Date agreement_start_date;
	private Date agreement_valid_till;
	private String agreement_remark;
	private double advance_paid;
	private Date advance_payment_date;

	/*@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "payment_mode", referencedColumnName = "mode")
	private PaymentMode paymentMode;
*/
	// private String payment_mode;
	private String payment_reference_no;
	private String agreement_status;
	private String update_by;
	

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name="update_timestamp")
	private Date   update_timestamp;
	public Date getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(java.util.Date update_timestamp) {
		this.update_timestamp = update_timestamp;
	}

	//private Timestamp update_timestamp;
	private Date renewal_mail_sent_date;
	private boolean is_renewal_confirmed;

	public Pr_Agreement() {
		// TODO Auto-generated constructor stub
	}

	public Pr_Agreement(int agreement_id, User auasubaua, boolean is_agreement_sent,
			Date agreement_sent_date, Date agreement_start_date, Date agreement_valid_till, String agreement_remark,
			double advance_paid, Date advance_payment_date, String payment_reference_no,
			String agreement_status, String update_by, Timestamp update_timestamp, Date renewal_mail_sent_date,
			boolean is_renewal_confirmed) {
		super();
		this.agreement_id = agreement_id;
		this.auasubaua = auasubaua;
		this.is_agreement_sent = is_agreement_sent;
		this.agreement_sent_date = agreement_sent_date;
		this.agreement_start_date = agreement_start_date;
		this.agreement_valid_till = agreement_valid_till;
		this.agreement_remark = agreement_remark;
		this.advance_paid = advance_paid;
		this.advance_payment_date = advance_payment_date;
	//	this.paymentMode = paymentMode;
		this.payment_reference_no = payment_reference_no;
		this.agreement_status = agreement_status;
		this.update_by = update_by;
		this.update_timestamp = update_timestamp;
		this.renewal_mail_sent_date = renewal_mail_sent_date;
		this.is_renewal_confirmed = is_renewal_confirmed;
	}

//	public int getAgreement_id() {
//		return agreement_id;
//	}
//
//	public void setAgreement_id(int agreement_id) {
//		this.agreement_id = agreement_id;
//	}
//
//	public User getAuasubaua() {
//		return auasubaua;
//	}
//
//	public void setAuasubaua(User auasubaua) {
//		this.auasubaua = auasubaua;
//	}
//
//	public boolean isIs_agreement_sent() {
//		return is_agreement_sent;
//	}
//
//	public void setIs_agreement_sent(boolean is_agreement_sent) {
//		this.is_agreement_sent = is_agreement_sent;
//	}
//
//	public Date getAgreement_sent_date() {
//		return agreement_sent_date;
//	}

//	public void setAgreement_sent_date(Date agreement_sent_date) {
//		this.agreement_sent_date = agreement_sent_date;
//	}
//
//	public Date getAgreement_start_date() {
//		return agreement_start_date;
//	}
//
//	public void setAgreement_start_date(Date agreement_start_date) {
//		this.agreement_start_date = agreement_start_date;
//	}
//
//	public Date getAgreement_valid_till() {
//		return agreement_valid_till;
//	}
//
//	public void setAgreement_valid_till(Date agreement_valid_till) {
//		this.agreement_valid_till = agreement_valid_till;
//	}
//
//	public String getAgreement_remark() {
//		return agreement_remark;
//	}

//	public void setAgreement_remark(String agreement_remark) {
//		this.agreement_remark = agreement_remark;
//	}
//
//	public double getAdvance_paid() {
//		return advance_paid;
//	}
//
//	public void setAdvance_paid(double advance_paid) {
//		this.advance_paid = advance_paid;
//	}
//
//	public Date getAdvance_payment_date() {
//		return advance_payment_date;
//	}
//
//	public void setAdvance_payment_date(Date advance_payment_date) {
//		this.advance_payment_date = advance_payment_date;
//	}

	/*public PaymentMode getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(PaymentMode paymentMode) {
		this.paymentMode = paymentMode;
	}*/

//	public String getPayment_reference_no() {
//		return payment_reference_no;
//	}
//
//	public void setPayment_reference_no(String payment_reference_no) {
//		this.payment_reference_no = payment_reference_no;
//	}
//
//	public String getAgreement_status() {
//		return agreement_status;
//	}
//
//	public void setAgreement_status(String agreement_status) {
//		this.agreement_status = agreement_status;
//	}
//
//	public String getUpdate_by() {
//		return update_by;
//	}
//
//	public void setUpdate_by(String update_by) {
//		this.update_by = update_by;
//	}

	/*public Timestamp getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(Timestamp update_timestamp) {
		this.update_timestamp = update_timestamp;
	}*/
//
//	public Date getRenewal_mail_sent_date() {
//		return renewal_mail_sent_date;
//	}
//
//	public void setRenewal_mail_sent_date(Date renewal_mail_sent_date) {
//		this.renewal_mail_sent_date = renewal_mail_sent_date;
//	}
//
//	public boolean isIs_renewal_confirmed() {
//		return is_renewal_confirmed;
//	}
//
//	public void setIs_renewal_confirmed(boolean is_renewal_confirmed) {
//		this.is_renewal_confirmed = is_renewal_confirmed;
//	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Pr_Agreement [agreement_id=" + agreement_id + ", auasubaua=" + auasubaua + ", is_agreement_sent="
				+ is_agreement_sent + ", agreement_sent_date=" + agreement_sent_date + ", agreement_start_date="
				+ agreement_start_date + ", agreement_valid_till=" + agreement_valid_till + ", agreement_remark="
				+ agreement_remark + ", advance_paid=" + advance_paid + ", advance_payment_date=" + advance_payment_date
				+ ", paymentMode=" + "" + ", payment_reference_no=" + payment_reference_no
				+ ", agreement_status=" + agreement_status + ", update_by=" + update_by + ", update_timestamp="
				+ update_timestamp + ", renewal_mail_sent_date=" + renewal_mail_sent_date + ", is_renewal_confirmed="
				+ is_renewal_confirmed + "]";
	}

}
