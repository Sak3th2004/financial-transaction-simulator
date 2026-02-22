package com.saketh.simulator.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * User account model for transaction processing.
 * Contains account balance and risk profile information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique user identifier
     */
    private String userId;

    /**
     * Full name of the account holder
     */
    private String name;

    /**
     * Email address
     */
    private String email;

    /**
     * Phone number
     */
    private String phone;

    /**
     * Current account balance
     */
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    /**
     * Account type (SAVINGS, CHECKING, BUSINESS)
     */
    @Builder.Default
    private String accountType = "SAVINGS";

    /**
     * Account status (ACTIVE, SUSPENDED, FROZEN)
     */
    @Builder.Default
    private String accountStatus = "ACTIVE";

    /**
     * Risk level (LOW, MEDIUM, HIGH)
     */
    @Builder.Default
    private String riskLevel = "LOW";

    /**
     * Country code (for geo-fencing)
     */
    private String country;

    /**
     * KYC verification status
     */
    @Builder.Default
    private Boolean kycVerified = false;

    /**
     * Account creation timestamp
     */
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Last login timestamp
     */
    private LocalDateTime lastLoginAt;

    /**
     * Total number of transactions made
     */
    @Builder.Default
    private Long totalTransactions = 0L;

    /**
     * Number of flagged transactions
     */
    @Builder.Default
    private Long flaggedTransactions = 0L;
}
