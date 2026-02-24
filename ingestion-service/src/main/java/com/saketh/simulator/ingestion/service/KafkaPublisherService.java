package com.saketh.simulator.ingestion.service;

import com.saketh.simulator.common.constants.KafkaTopics;
import com.saketh.simulator.common.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service for publishing transaction events to Kafka.
 * Handles async publishing with callbacks and error handling.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaPublisherService {

    private final KafkaTemplate<String, Transaction> kafkaTemplate;
    
    // Metrics
    private final AtomicLong publishedCount = new AtomicLong(0);
    private final AtomicLong failedCount = new AtomicLong(0);

    /**
     * Publish transaction to Kafka asynchronously
     */
    public CompletableFuture<SendResult<String, Transaction>> publishTransaction(Transaction transaction) {
        log.debug("Publishing transaction {} to Kafka topic: {}", 
            transaction.getTransactionId(), KafkaTopics.RAW_TRANSACTIONS);
        
        CompletableFuture<SendResult<String, Transaction>> future = 
            kafkaTemplate.send(
                KafkaTopics.RAW_TRANSACTIONS, 
                transaction.getTransactionId(), 
                transaction
            );
        
        // Add callback for success/failure handling
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                publishedCount.incrementAndGet();
                log.info("Transaction {} published successfully to partition {} with offset {}",
                    transaction.getTransactionId(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            } else {
                failedCount.incrementAndGet();
                log.error("Failed to publish transaction {}: {}", 
                    transaction.getTransactionId(), ex.getMessage(), ex);
            }
        });
        
        return future;
    }

    /**
     * Publish transaction synchronously (blocking)
     * Use this when you need immediate confirmation
     */
    public void publishTransactionSync(Transaction transaction) throws Exception {
        log.debug("Publishing transaction {} synchronously", transaction.getTransactionId());
        
        SendResult<String, Transaction> result = kafkaTemplate.send(
            KafkaTopics.RAW_TRANSACTIONS,
            transaction.getTransactionId(),
            transaction
        ).get(); // Block until complete
        
        publishedCount.incrementAndGet();
        log.info("Transaction {} published synchronously to partition {} with offset {}",
            transaction.getTransactionId(),
            result.getRecordMetadata().partition(),
            result.getRecordMetadata().offset());
    }

    /**
     * Get total published count
     */
    public long getPublishedCount() {
        return publishedCount.get();
    }

    /**
     * Get total failed count
     */
    public long getFailedCount() {
        return failedCount.get();
    }
}
