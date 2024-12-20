/**
 * 
 */
package com.cdac.mumbai.model;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;




public class Email implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4774700849392276794L;
	private String to;
	private String cc;
	private String subject;
	private String mailcontent;
	private String signature;
	private MultipartFile[] attach;
	private int reg_id;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailcontent() {
		return mailcontent;
	}
	public void setMailcontent(String mailcontent) {
		this.mailcontent = mailcontent;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public MultipartFile[] getAttach() {
		return attach;
	}
	public void setAttach(MultipartFile[] attach) {
		this.attach = attach;
	}
	public int getReg_id() {
		return reg_id;
	}
	public void setReg_id(int reg_id) {
		this.reg_id = reg_id;
	}
	public Email(String to, String cc, String subject, String mailcontent, String signature, MultipartFile[] attach,
			int reg_id) {
		super();
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.mailcontent = mailcontent;
		this.signature = signature;
		this.attach = attach;
		this.reg_id = reg_id;
	}

	/**
	 * 
	 */
	public Email() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Email [to=" + to + ", cc=" + cc + ", subject=" + subject + ", mailcontent=" + mailcontent
				+ ", signature=" + signature + ", attach=" + Arrays.toString(attach) + ", reg_id=" + reg_id + "]";
	}
}
