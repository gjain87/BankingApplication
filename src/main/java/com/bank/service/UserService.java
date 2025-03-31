package com.bank.service;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);

	String nameEnquiry(EnquiryRequest enquiryRequest);
	
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
	
	BankResponse trasfer(TransferRequest request);
	
	BankResponse login(LoginDto loginDto);
}
