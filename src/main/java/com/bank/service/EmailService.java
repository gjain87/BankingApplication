package com.bank.service;

import com.bank.dto.EmailDetails;

import jakarta.mail.MessagingException;

public interface EmailService {

	void sendEmail(EmailDetails emailDetails);
	
	void sendEmailWithAttachment(EmailDetails emailDetails) throws MessagingException;
}
