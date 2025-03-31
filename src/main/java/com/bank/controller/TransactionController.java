package com.bank.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.entity.Transaction;
import com.bank.service.BankStatement;
import com.itextpdf.text.DocumentException;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/bankstatement")
public class TransactionController {
	
	@Autowired
	private BankStatement bankStatement;
	
	@GetMapping("/generate")
	public List<Transaction>generateStatement(@RequestParam String accountNumber,@RequestParam String startDate, @RequestParam String endDate) throws FileNotFoundException, DocumentException, MessagingException
	{
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
	}

}
