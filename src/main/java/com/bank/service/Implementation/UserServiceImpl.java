package com.bank.service.Implementation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bank.config.JwtTokenProvider;
import com.bank.dto.AccountInfo;
import com.bank.dto.BankResponse;
import com.bank.dto.CreditDebitRequest;
import com.bank.dto.EmailDetails;
import com.bank.dto.EnquiryRequest;
import com.bank.dto.LoginDto;
import com.bank.dto.TransactionDto;
import com.bank.dto.TransferRequest;
import com.bank.dto.UserRequest;
import com.bank.entity.Role;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import com.bank.service.EmailService;
import com.bank.service.TransactionService;
import com.bank.service.UserService;
import com.bank.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		
		if(userRepository.existsByEmail(userRequest.getEmail())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_EXIST_CODE).responseMessage(AccountUtils.ACCOUNT_EXIST_MESSAGE).accountInfo(null).build();
		}
		
		User newUser=User.builder().firstName(userRequest.getFirstName()).lastName(userRequest.getLastName()).gender(userRequest.getGender()).address(userRequest.getAddress())
			.email(userRequest.getEmail()).password(passwordEncoder.encode(userRequest.getPassword())).phoneNumber(userRequest.getPhoneNumber()).alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
			.accountNumber(AccountUtils.generateAccountNumber()).accountBalance(BigDecimal.ZERO).status("ACTIVE").role(Role.valueOf("ROLE_ADMIN")).build();
		User savedUser = userRepository.save(newUser);
		
		//send email alert
		EmailDetails emailDetails=EmailDetails.builder().recipent(savedUser.getEmail()).subject("Account Creation ")
				.messageBody("Congratulations you account has been successfully created...\n Your Account Details:\n"
				+ "Account Name: "+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+"Account"+"\n Account Number: "+savedUser.getAccountNumber()).build();
		emailService.sendEmail(emailDetails);
		AccountInfo accountInfo = AccountInfo.builder().accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+"Account").accountBalance(savedUser.getAccountBalance()).accountNumber(savedUser.getAccountNumber()).build();
		return BankResponse.builder().accountInfo(accountInfo).responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE).responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE).build();
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		
		if(!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOESNOT_EXIST_CODE).responseMessage(AccountUtils.ACCOUNT_DOESNOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return BankResponse.builder().accountInfo(AccountInfo.builder()
				.accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+"Account")
				.accountBalance(foundUser.getAccountBalance())
				.accountNumber(foundUser.getAccountNumber()).build())
				.responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
				.responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE).build();
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		if(!userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
			return AccountUtils.ACCOUNT_DOESNOT_EXIST_MESSAGE;
		}
		User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
		return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+"Account";
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
		Boolean accountNumber = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		if(!accountNumber) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOESNOT_EXIST_CODE).responseMessage(AccountUtils.ACCOUNT_DOESNOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		
		User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
		userRepository.save(userToCredit);
		
		//save transaction
		TransactionDto transactionDto=TransactionDto.builder()
				.accountNumber(userToCredit.getAccountNumber())
				.amount(creditDebitRequest.getAmount())
				.transactionType("CREDIT")
				.build();
		transactionService.saveTransaction(transactionDto);
		
		return BankResponse.builder()
				.accountInfo(AccountInfo.builder()
						.accountBalance(userToCredit.getAccountBalance())
						.accountNumber(userToCredit.getAccountNumber())
						.accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+"Account")
						.build())
				.responseCode(AccountUtils.ACCOUNT_CREDITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
				.build();
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
		Boolean accountNumber = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
		if(!accountNumber) {
			return BankResponse.builder().responseCode(AccountUtils.ACCOUNT_DOESNOT_EXIST_CODE).responseMessage(AccountUtils.ACCOUNT_DOESNOT_EXIST_MESSAGE).accountInfo(null).build();
		}
		User userToDebit=userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
		if(creditDebitRequest.getAmount().compareTo(userToDebit.getAccountBalance())==1) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_CODE)
					.responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountBalance(userToDebit.getAccountBalance())
							.accountNumber(userToDebit.getAccountNumber())
							.accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+"Account")
							.build())
					.build();
		}
		userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(creditDebitRequest.getAmount()));
		userRepository.save(userToDebit);
		
		//save transaction
				TransactionDto transactionDto=TransactionDto.builder()
						.accountNumber(userToDebit.getAccountNumber())
						.amount(creditDebitRequest.getAmount())
						.transactionType("DEBIT")
						.build();
				transactionService.saveTransaction(transactionDto);
				
		return BankResponse.builder()
				.accountInfo(AccountInfo.builder()
						.accountBalance(userToDebit.getAccountBalance())
						.accountNumber(userToDebit.getAccountNumber())
						.accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+"Account")
						.build())
				.responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
				.responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
				.build();
	}

	@Override
	public BankResponse trasfer(TransferRequest request) {
		boolean isValidSourceAccount=userRepository.existsByAccountNumber(request.getSourceAccountNumber());
		boolean isValidDestinationAccount=userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
		
		if(!isValidSourceAccount ) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INVALID_SOURCE_ACCOUNT_CODE)
					.responseMessage(AccountUtils.INVALID_SOURCE_ACCOUNT_MESSAGE)
					.accountInfo(null)
					.build();
		}
		if(!isValidDestinationAccount ) {
			return BankResponse.builder()
					.responseCode(AccountUtils.INVALID_DESTINATION_ACCOUNT_CODE)
					.responseMessage(AccountUtils.INVALID_DESTINATION_ACCOUNT_MESSAGE)
					.accountInfo(null)
					.build();
		}
		
		
		User senderAccount = userRepository.findByAccountNumber(request.getSourceAccountNumber());
		User recieverAccount=userRepository.findByAccountNumber(request.getDestinationAccountNumber());
		
		if(senderAccount.getAccountNumber()==recieverAccount.getAccountNumber()) {
			return BankResponse.builder()
					.responseCode(AccountUtils.SAME_SOURCE_AND_DESTINATION_CODE)
					.responseMessage(AccountUtils.SAME_SOURCE_AND_DESTINATION_MESSAGE)
					.accountInfo(AccountInfo.builder().accountBalance(senderAccount.getAccountBalance()).accountNumber(senderAccount.getAccountNumber()).accountName(senderAccount.getFirstName()+" "+senderAccount.getLastName()+" "+"Account").build())
					.build();
		}
		
		if(request.getAmount().compareTo(senderAccount.getAccountBalance())==1) {
			return BankResponse.builder()
					.responseCode(AccountUtils.ACCOUNT_INSUFFICIENT_CODE)
					.responseMessage(AccountUtils.ACCOUNT_INSUFFICIENT_MESSAGE)
					.accountInfo(AccountInfo.builder()
							.accountBalance(senderAccount.getAccountBalance())
							.accountNumber(senderAccount.getAccountNumber())
							.accountName(senderAccount.getFirstName()+" "+senderAccount.getLastName()+" "+"Account")
							.build())
					.build();
		}
		senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(request.getAmount()));
		recieverAccount.setAccountBalance(recieverAccount.getAccountBalance().add(request.getAmount()));
		userRepository.save(senderAccount);
		userRepository.save(recieverAccount);
		
		//save debit transaction
				TransactionDto debitTransaction=TransactionDto.builder()
						.accountNumber(senderAccount.getAccountNumber())
						.amount(request.getAmount())
						.transactionType("DEBIT")
						.build();
				transactionService.saveTransaction(debitTransaction);
				

		//save credit transaction
				TransactionDto creditTransaction=TransactionDto.builder()
						.accountNumber(recieverAccount.getAccountNumber())
						.amount(request.getAmount())
						.transactionType("CREDIT")
						.build();
				transactionService.saveTransaction(creditTransaction);
		
		EmailDetails debitAlert=EmailDetails.builder()
				.subject("DEBIT ALERT")
				.recipent(senderAccount.getEmail())
				.messageBody("The sum of Rs."+request.getAmount()+" was debited from your account "+senderAccount.getAccountNumber()+" .Your current available balance is Rs."+senderAccount.getAccountBalance())
				.build();
		emailService.sendEmail(debitAlert);
		
		EmailDetails creditAlert=EmailDetails.builder()
				.subject("CREDIT ALERT")
				.recipent(recieverAccount.getEmail())
				.messageBody("The sum of Rs."+request.getAmount()+" was credited to your account "+recieverAccount.getAccountNumber()+" .Your current available balance is Rs."+recieverAccount.getAccountBalance())
				.build();
		emailService.sendEmail(creditAlert);
		
		return BankResponse.builder().responseCode(AccountUtils.SOURCE_TO_DESTINATION_TRANSFER_CODE)
				.responseMessage(AccountUtils.SOURCE_TO_DESTINATION_TRANSFER_MESSAGE)
				.accountInfo(AccountInfo.builder().accountBalance(senderAccount.getAccountBalance()).accountNumber(senderAccount.getAccountNumber()).accountName(senderAccount.getFirstName()+" "+senderAccount.getLastName()+" "+"Account").build())
				.build();

	}
	
	public BankResponse login(LoginDto loginDto) {
		Authentication authentication=null;
		authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
		EmailDetails loginAlert=EmailDetails.builder()
				.subject("LOGIN ALERT")
				.recipent(loginDto.getEmail())
				.messageBody("You logged into your account. If you did not initiate this request, please contact your nearest bank immediately.")
				.build();
		emailService.sendEmail(loginAlert);
		return BankResponse.builder()
				.responseCode(AccountUtils.USER_LOGIN_SUCCESSFUL_CODE)
				.responseMessage(jwtTokenProvider.generateToken(authentication))
				.build();
		
	}

	

}

