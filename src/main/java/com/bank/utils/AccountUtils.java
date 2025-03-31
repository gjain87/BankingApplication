package com.bank.utils;

import java.time.Year;

public class AccountUtils {
	
	public static final String ACCOUNT_EXIST_CODE="001";
	public static final String ACCOUNT_EXIST_MESSAGE="THIS USER ALREADY HAS AN ACCOUNT CREATED!!";
	public static final String ACCOUNT_DOESNOT_EXIST_CODE="003";
	public static final String ACCOUNT_DOESNOT_EXIST_MESSAGE="THIS ACCOUNT NUMBER DOESNOT EXIST...";
	public static final String ACCOUNT_CREATION_SUCCESS_CODE="002";
	public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE="ACCOUNT CREATED SUCCESSFULLY";
	public static final String ACCOUNT_FOUND_CODE="004";
	public static final String ACCOUNT_FOUND_MESSAGE="USER ACCOUND FOUND!!";
	public static final String ACCOUNT_CREDITED_CODE="005";
	public static final String ACCOUNT_CREDITED_MESSAGE="THE ACCOUNT WAS CREDITED SUCCESSFULLY!!!";
	public static final String ACCOUNT_DEBITED_CODE="006";
	public static final String ACCOUNT_DEBITED_MESSAGE="THE ACCOUNT WAS DEBITED SUCCESSFULLY!!!";
	public static final String ACCOUNT_INSUFFICIENT_CODE="007";
	public static final String ACCOUNT_INSUFFICIENT_MESSAGE="THE ACCOUNT HAS INSUFFICIENT BALANCE!!!";
	public static final String INVALID_SOURCE_ACCOUNT_CODE="008";
	public static final String INVALID_SOURCE_ACCOUNT_MESSAGE="THE ACCOUNT IS NOT VALID!!!";
	public static final String INVALID_DESTINATION_ACCOUNT_CODE="009";
	public static final String INVALID_DESTINATION_ACCOUNT_MESSAGE="THE ACCOUNT IS NOT VALID!!!";
	public static final String SOURCE_TO_DESTINATION_TRANSFER_CODE="010";
	public static final String SOURCE_TO_DESTINATION_TRANSFER_MESSAGE="TRANSFER WAS SUCCESSFULL..";
	public static final String SAME_SOURCE_AND_DESTINATION_CODE="011";
	public static final String SAME_SOURCE_AND_DESTINATION_MESSAGE="BOTH SENDER AND RECIEVER ARE SAME..";
	public static final String USER_LOGIN_SUCCESSFUL_CODE="012";
	public static final String  USER_LOGIN_SUCCESSFUL_MESSAGE="YOU HAVE LOGGED IN SUCCESSFULLY..";
	
	
	public static String generateAccountNumber() {
		Year currentYear=Year.now();
		int min=100000;
		int max=999999;
		int random=(int) Math.round(Math.random()*(max-min+1)+min);
		String year=String.valueOf(currentYear);
		String randomNumber=String.valueOf(random);
		StringBuilder accountNumber=new StringBuilder();
		accountNumber.append(year).append(randomNumber);
		return accountNumber.toString();
	}
}
