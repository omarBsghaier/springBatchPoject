package com.example.springbatchexample.Entity;

import com.example.springbatchexample.Repository.BankTransactionRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class BankTransactionItemWriter implements ItemWriter<BankTransaction> {
    @Autowired
    BankTransactionRepository bankTransactionRepository ;


    @Override
    public void write(List<? extends BankTransaction> chunk) throws Exception {
        bankTransactionRepository.saveAll(chunk);

    }
}
