package com.bank.service.Implementation;

import java.io.File;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.bank.dto.EmailDetails;
import com.bank.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String senderEmail;

	@Override
	public void sendEmail(EmailDetails emailDetails) {
		try {
			SimpleMailMessage mailMessage=new SimpleMailMessage();
			mailMessage.setFrom(senderEmail);
			mailMessage.setTo(emailDetails.getRecipent());
			mailMessage.setText(emailDetails.getMessageBody());
			mailMessage.setSubject(emailDetails.getSubject());
			
			javaMailSender.send(mailMessage);
			System.out.println("Mail Sent Successfully....");
		} catch (Exception e) {
			throw new RuntimeException();		}
		
	}

	@Override
	public void sendEmailWithAttachment(EmailDetails emailDetails) throws MessagingException {
		MimeMessage  message=javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(message,true);
		mimeMessageHelper.setFrom(senderEmail);
		mimeMessageHelper.setTo(emailDetails.getRecipent());
		mimeMessageHelper.setText(emailDetails.getMessageBody());
		mimeMessageHelper.setSubject(emailDetails.getSubject());
		
		FileSystemResource file=new FileSystemResource(new File(emailDetails.getAttachment()));
		mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getFilename()), file);
		javaMailSender.send(message);
		
	}

}
