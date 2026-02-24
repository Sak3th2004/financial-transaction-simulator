package com.saketh.simulator.ingestion.service;

import com.saketh.simulator.common.model.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service layer for transaction ingestion business logic.
 * Validates, enriches, and publishes transactions to Kafka.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionIngestionService {

    // In-memory counters for statistics (will be replaced with proper metrics)
    private final AtomicLong totalReceived = new AtomicLong(0);
    private final AtomicLong totalPublished = new AtomicLong(0);
    private final AtomicLong totalFailed = new AtomicLong(0);

    /**
     * Ingest a single transaction
     */
    public String ingestTransaction(Transaction transaction) {
        log.debug("Processing transaction: {}", transaction.getTransactionId());
        
        // Increment received counter
        totalReceived.incrementAndGet();
        
        // Enrich transaction with processing metadata
        enrichTransaction(transaction);
        
        // Validate transaction (basic validation)
        validateTransaction(transaction);
        
        // TODO: Publish to Kafka (will be implemented in next phase)
        // For now, just log and simulate success
        log.info("Transaction {} ready for publishing to Kafka", 
            transaction.getTransactionId());
        
        totalPublished.incrementAndGet();
        
        return "Transaction accepted for processing";
    }

    /**
     * Ingest multiple transactions in batch
     */
    public int ingestBatch(List<Transaction> transactions) {
        log.info("Processing batch of {} transactions", transactions.size());
        
        int successCount = 0;
        for (Transaction transaction : transactions) {
            try {
                ingestTransaction(transaction);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to process transaction {} in batch: {}", 
                    transaction.getTransactionId(), e.getMessage());
                totalFailed.incrementAndGet();
            }
        }
        
        return successCount;
    }

    /**
     * Enrich transaction with additional metadata
     */
    private void enrichTransaction(Transaction transaction) {
        // Set timestamp if not provided
        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(LocalDateTime.now());
        }
        
        // Set default status if not provided
        if (transaction.getStatus() == null || transaction.getStatus().isEmpty()) {
            transaction.setStatus("PENDING");
        }
        
        // Set default currency if not provided
        if (transaction.getCurrency() == null || transaction.getCurrency().isEmpty()) {
            transaction.setCurrency("USD");
        }
        
        log.debug("Transaction {} enriched with metadata", transaction.getTransactionId());
    }

    /**
     * Validate transaction business rules
     */
    private void validateTransaction(Transaction transaction) {
        // Basic validation (Bean Validation handles most of this)
        if (transaction.getAmount() == null) {
            throw new IllegalArgumentException("Transaction amount cannot be null");
        }
        
        if (transaction.getAmount().doubleValue() <= 0) {
            throw new IllegalArgumentException("Transaction amount must be positive");
        }
        
        if (transaction.getAmount().doubleValue() > 100000) {
            log.warn("High-value transaction detected: {} - Amount: {}", 
                transaction.getTransactionId(), transaction.getAmount());
        }
        
        log.debug("Transaction {} passed validation", transaction.getTransactionId());
    }

    /**
     * Get ingestion statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReceived", totalReceived.get());
        stats.put("totalPublished", totalPublished.get());
        stats.put("totalFailed", totalFailed.get());
        stats.put("successRate", calculateSuccessRate());
        stats.put("timestamp", LocalDateTime.now().toString());
        
        return stats;
    }

    private double calculateSuccessRate() {
        long received = totalReceived.get();
        if (received == 0) {
            return 100.0;
        }
        return (totalPublished.get() * 100.0) / received;
    }
}
