package com.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	@PostMapping("/login")
	public BankResponse Login(@RequestBody LoginDto loginDto) {
		return userService.login(loginDto);
	}
	
	@Operation(summary = "This API is used to create a new Bank Account for the user. It takes User's personal Information and creates an Account.")
	@PostMapping("/create")
	public BankResponse createBankAccount(@RequestBody UserRequest userRequest) {
		BankResponse response = userService.createAccount(userRequest);
		return response;
	}
	
	@GetMapping("/balanceEnquiry")
	public BankResponse BalanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		BankResponse balanceEnquiry = userService.balanceEnquiry(enquiryRequest);
		return balanceEnquiry;
	}
	
	@GetMapping("/nameEnquiry")
	public String NameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		String nameEnquiry = userService.nameEnquiry(enquiryRequest);
		return nameEnquiry;
	}
	
	@PostMapping("/credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.creditAccount(creditDebitRequest);
	}
	
	@PostMapping("/debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.debitAccount(creditDebitRequest);
	}
	
	@PostMapping("transfer")
	public BankResponse Transfer(@RequestBody TransferRequest request) {
		return userService.trasfer(request);
	}

}
