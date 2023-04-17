package com.example.springbatchexample.Repository;

import com.example.springbatchexample.Entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction,Long> {
}
