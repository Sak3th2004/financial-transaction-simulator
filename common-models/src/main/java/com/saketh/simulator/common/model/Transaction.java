package com.saketh.simulator.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Core transaction domain model shared across all microservices.
 * Represents a financial transaction with fraud detection metadata.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    /**
     * Unique transaction identifier
     */
    @Builder.Default
    private String transactionId = UUID.randomUUID().toString();

    /**
     * Source user/account ID
     */
    @NotNull(message = "User ID cannot be null")
    private String userId;

    /**
     * Transaction amount (always positive)
     */
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    /**
     * Transaction type (DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT)
     */
    @NotNull(message = "Transaction type cannot be null")
    private String transactionType;

    /**
     * Target account for transfers
     */
    private String targetAccountId;

    /**
     * Current transaction status
     */
    @Builder.Default
    private String status = "PENDING";

    /**
     * Timestamp when transaction was initiated
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Merchant/vendor details (for payments)
     */
    private String merchantId;

    /**
     * Transaction category (e.g., groceries, entertainment)
     */
    private String category;

    /**
     * Geographic location (latitude,longitude)
     */
    private String location;

    /**
     * Device fingerprint for fraud detection
     */
    private String deviceId;

    /**
     * IP address of the transaction origin
     */
    private String ipAddress;

    /**
     * Currency code (USD, EUR, INR, etc.)
     */
    @Builder.Default
    private String currency = "USD";

    /**
     * Fraud flag set by validation service
     */
    @Builder.Default
    private Boolean fraudFlag = false;

    /**
     * ML-generated fraud probability score (0.0 to 1.0)
     */
    private Double fraudScore;

    /**
     * Reason for fraud flag (if any)
     */
    private String fraudReason;

    /**
     * Additional metadata in JSON format
     */
    private String metadata;

    /**
     * Timestamp when processed by validation service
     */
    private LocalDateTime processedAt;

    /**
     * Timestamp when settled
     */
    private LocalDateTime settledAt;

    /**
     * Processing notes or error messages
     */
    private String notes;
}
