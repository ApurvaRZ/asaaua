package com.cdac.mumbai.service;

import com.cdac.mumbai.model.Email;

public interface EmailService {
	public boolean sendEmail(Email email, String smtphost, String smtpport, String smtpuser, String smtppass, String emailids);
}
