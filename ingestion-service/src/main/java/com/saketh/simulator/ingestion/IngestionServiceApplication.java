package com.saketh.simulator.ingestion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Ingestion Service - Entry point for financial transactions.
 * Receives transactions via REST API and publishes to Kafka for processing.
 */
@SpringBootApplication
@EnableKafka
public class IngestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IngestionServiceApplication.class, args);
    }
}
