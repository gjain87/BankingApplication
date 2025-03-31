package com.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
