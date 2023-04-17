package com.example.springbatchexample.Configuration;

import com.example.springbatchexample.Entity.BankTransaction;
import com.example.springbatchexample.Entity.BankTransactionItemAnalyticsProcessor;
import com.example.springbatchexample.Entity.BankTransactionItemProcessor;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

@Configuration
@EnableBatchProcessing
public class SprinBatchConfig {
    @Autowired private  StepBuilderFactory stepBuilderFactory ;
    @Autowired private  JobBuilderFactory jobBuilderFactory ;
    @Autowired private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired private ItemWriter<BankTransaction> bankTransactionItemWriter;
   // @Autowired  private ItemProcessor<BankTransaction, BankTransaction> bankTransactionItemProcessor;

        @Bean
        public Job BankJob(JobRepository jobRepository){
            Step step1=  stepBuilderFactory.get("ETL-Transaction-File-Load" )
                    .<BankTransaction, BankTransaction>chunk(100 )
                    .reader(bankTransactionItemReader)
                    .writer(bankTransactionItemWriter)
                    .processor(compositeItemProcessor())
                    .build();
         return   jobBuilderFactory.get("ETL-Load")
                .start(step1)
                .build();
    }
  @Bean
    public ItemProcessor<? super BankTransaction,? extends BankTransaction> compositeItemProcessor() {
      List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessors= new ArrayList<>();
      itemProcessors.add(itemProcessor1());
      itemProcessors.add(itemProcessor2());
      CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor=new CompositeItemProcessor<>();
      compositeItemProcessor.setDelegates(itemProcessors);
      return compositeItemProcessor;

  }
    @Bean
    BankTransactionItemProcessor itemProcessor1(){
        return new BankTransactionItemProcessor(); }
    @Bean
     BankTransactionItemAnalyticsProcessor itemProcessor2(){
            return new BankTransactionItemAnalyticsProcessor();}
    @Bean
    public FlatFileItemReader<BankTransaction> getItemReader( @Value("${inputFile}") Resource resource) {
        FlatFileItemReader<BankTransaction> flatFileItemReader = new FlatFileItemReader<>();
       flatFileItemReader.setName("CSV-READER");
       flatFileItemReader.setLinesToSkip(1);
       flatFileItemReader. setResource(resource);
       flatFileItemReader.setLineMapper(lineMapper());
       return flatFileItemReader;
    }

    @Bean
    public LineMapper<BankTransaction> lineMapper(){
    DefaultLineMapper<BankTransaction> lineMapper=new DefaultLineMapper<>();
    DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
    lineTokenizer.setDelimiter(",");
    lineTokenizer.setStrict(false);
    lineTokenizer.setNames("id", "accountID", "strDate", "transactionType", "amount");
    lineMapper.setLineTokenizer(lineTokenizer);
    BeanWrapperFieldSetMapper<BankTransaction> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
    fieldSetMapper.setTargetType (BankTransaction.class);
    lineMapper.setFieldSetMapper(fieldSetMapper);
     return lineMapper;}


}