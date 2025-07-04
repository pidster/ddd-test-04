package com.lakesidemutual.risk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for the Lakeside Mutual Risk Service.
 * 
 * This service handles risk assessment and pricing calculations for insurance policies,
 * implementing Domain-Driven Design principles within the Risk Assessment bounded context.
 */
@SpringBootApplication
@EnableKafka
@EnableTransactionManagement
public class RiskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskServiceApplication.class, args);
    }
}
