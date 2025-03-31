package com.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TransferRequest {
	private String sourceAccountNumber;
	private String destinationAccountNumber;
	private BigDecimal amount;
	
	
}
