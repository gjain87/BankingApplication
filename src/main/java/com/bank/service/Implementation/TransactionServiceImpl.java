package com.bank.service.Implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank.dto.TransactionDto;
import com.bank.entity.Transaction;
import com.bank.repository.TransactionRepository;
import com.bank.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	private TransactionRepository transactionRepository;
	
	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		Transaction transaction = Transaction.builder()
		.transactionType(transactionDto.getTransactionType())
		.accountNumber(transactionDto.getAccountNumber())
		.amount(transactionDto.getAmount())
		.status("SUCCESS")
		.build();
		transactionRepository.save(transaction);
		System.out.println("Transaction Saved Successfully!!");
	}

}
