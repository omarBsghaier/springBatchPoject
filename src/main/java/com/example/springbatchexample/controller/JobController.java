package com.example.springbatchexample.controller;

import com.example.springbatchexample.Entity.BankTransaction;
import com.example.springbatchexample.Entity.BankTransactionItemAnalyticsProcessor;
import lombok.NonNull;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
public class JobController {
    @Autowired private JobLauncher jobLauncher;
    @Autowired private Job job;
    @Autowired private BankTransactionItemAnalyticsProcessor analyticsProcessor;

    @GetMapping("/startJob")
    public BatchStatus load() throws Exception {
        Map<String, JobParameter> params = new HashMap();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        while (jobExecution.isRunning())
            System.out.println(".....");

        return jobExecution.getStatus();

    }

    @GetMapping("/analytics")
    public Map<String, Double> analytics() {
        Map<String, Double> map = new HashMap<>();
        map.put("totalCredit", analyticsProcessor.getTotalCredit());
        map.put("totalDebit", analyticsProcessor.getTotalDebit());
        return map;
    }

}
