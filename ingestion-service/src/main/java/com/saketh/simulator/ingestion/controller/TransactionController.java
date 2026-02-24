package com.saketh.simulator.ingestion.controller;

import com.saketh.simulator.common.model.Transaction;
import com.saketh.simulator.ingestion.service.TransactionIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for transaction ingestion.
 * Provides endpoints to submit single or batch transactions.
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionIngestionService ingestionService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "ingestion-service");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }

    /**
     * Ingest a single transaction
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> ingestTransaction(
            @Valid @RequestBody Transaction transaction) {
        
        log.info("Received transaction ingestion request: {}", transaction.getTransactionId());
        
        try {
            String result = ingestionService.ingestTransaction(transaction);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("transactionId", transaction.getTransactionId());
            response.put("message", result);
            response.put("status", "PENDING");
            
            log.info("Transaction {} ingested successfully", transaction.getTransactionId());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            
        } catch (Exception e) {
            log.error("Failed to ingest transaction {}: {}", 
                transaction.getTransactionId(), e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("transactionId", transaction.getTransactionId());
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Ingest multiple transactions in batch
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> ingestTransactionBatch(
            @Valid @RequestBody List<Transaction> transactions) {
        
        log.info("Received batch ingestion request with {} transactions", transactions.size());
        
        try {
            int successCount = ingestionService.ingestBatch(transactions);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("totalReceived", transactions.size());
            response.put("successCount", successCount);
            response.put("message", "Batch ingestion completed");
            
            log.info("Batch ingestion completed: {}/{} successful", 
                successCount, transactions.size());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
            
        } catch (Exception e) {
            log.error("Batch ingestion failed: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("totalReceived", transactions.size());
            errorResponse.put("error", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get transaction statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = ingestionService.getStatistics();
        return ResponseEntity.ok(stats);
    }
}
