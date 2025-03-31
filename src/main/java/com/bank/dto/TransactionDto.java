package com.bank.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDto {
	private String transactionType;
	private String accountNumber;
	private BigDecimal amount;
	private String status;
}
