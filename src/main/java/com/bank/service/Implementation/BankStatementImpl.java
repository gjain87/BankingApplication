package com.bank.service.Implementation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.EmailDetails;
import com.bank.entity.Transaction;
import com.bank.entity.User;
import com.bank.repository.TransactionRepository;
import com.bank.repository.UserRepository;
import com.bank.service.BankStatement;
import com.bank.service.EmailService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BankStatementImpl implements BankStatement {
	
	private static final String FILE="D:\\Boot\\BankingApplication\\MyStatement.pdf";
	
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	public List<Transaction>generateStatement(String accountNumber,String startDate, String endDate) throws FileNotFoundException, DocumentException, MessagingException{
		LocalDate start=LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
		LocalDate end=LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
		List<Transaction> transactionList = transactionRepository.findAll()
				.stream()
				.filter(transaction->transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction->transaction.getCreatedAt().isAfter(start) || transaction.getCreatedAt().isEqual(start) )
				.filter(transaction->transaction.getModifiedAt().isBefore(end) || transaction.getCreatedAt().isEqual(end)).toList();
		User user = userRepository.findByAccountNumber(accountNumber);
		String customerName=user.getFirstName()+" "+user.getLastName();
	
		Rectangle statementSize=new Rectangle(PageSize.A4);
		Document document=new Document(statementSize);
		log.info("setting size of the statement pdf");
		OutputStream outputStream=new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		PdfPTable bankInfoTable=new PdfPTable(1);
		PdfPCell bankName=new PdfPCell(new Phrase("HDFC BANK"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.RED);
		bankName.setPadding(20f);
		
		PdfPCell bankAddress=new PdfPCell(new Phrase("CyberHub, Gurgaon, Haryana"));
		bankAddress.setBorder(0);
		
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);
		
		PdfPTable statementInfo=new PdfPTable(2);
		PdfPCell customerInfo=new PdfPCell(new Phrase("Start Date: "+startDate));
		customerInfo.setBorder(0);
		PdfPCell statement=new PdfPCell(new Phrase("Statement of Account: "+accountNumber));
		statement.setBorder(0);
		PdfPCell stopDate=new PdfPCell(new Phrase("End Date: "+endDate));
		stopDate.setBorder(0);
		PdfPCell name=new PdfPCell(new Phrase("Customer Name: "+customerName));
		name.setBorder(0);
		PdfPCell space=new PdfPCell();
		space.setBorder(0);
		PdfPCell address=new PdfPCell(new Phrase("Customer Address: "+user.getAddress()));
		address.setBorder(0);
		
		
		PdfPTable transactionsTable=new PdfPTable(4);
		PdfPCell date=new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell transactionType=new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell transactionAmount=new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		PdfPCell status=new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);
		
		
		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(status);
		
		transactionList.forEach(transaction->{
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
			transactionsTable.addCell(new Phrase(transaction.getStatus()));
			
		});
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(name);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionsTable);
		
		document.close();
		EmailDetails emailDetails=EmailDetails.builder()
				.recipent(user.getEmail())
				.subject("Statement for Time Period "+startDate+" to "+endDate)
				.messageBody("Kindly find your requested account statement attached")
				.attachment(FILE)
				.build();
		emailService.sendEmailWithAttachment(emailDetails);
		
		return transactionList;
	}
	
	

}
