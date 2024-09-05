package com.blossom.eBank.repository;

import com.blossom.eBank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByRef(String ref);
}
