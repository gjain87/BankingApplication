package com.bank.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.bank.entity.Transaction;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;

public interface BankStatement {
	
	List<Transaction>generateStatement(String accountNumber, String startDate,String endDate) throws FileNotFoundException, DocumentException, MessagingException;

}
