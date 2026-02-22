package com.saketh.simulator.common.enums;

/**
 * Transaction processing status across the event pipeline.
 */
public enum TransactionStatus {
    
    /**
     * Transaction received, awaiting validation
     */
    PENDING,
    
    /**
     * Currently being validated
     */
    VALIDATING,
    
    /**
     * Passed validation, ready for settlement
     */
    VALIDATED,
    
    /**
     * Failed validation checks
     */
    VALIDATION_FAILED,
    
    /**
     * Flagged as potentially fraudulent, needs review
     */
    FRAUD_REVIEW,
    
    /**
     * Being processed for settlement
     */
    SETTLING,
    
    /**
     * Successfully settled
     */
    SETTLED,
    
    /**
     * Settlement failed
     */
    SETTLEMENT_FAILED,
    
    /**
     * Rejected due to fraud/policy violation
     */
    REJECTED,
    
    /**
     * Cancelled by user
     */
    CANCELLED,
    
    /**
     * Refunded/reversed
     */
    REFUNDED;
    
    /**
     * Check if transaction is in a terminal state
     */
    public boolean isTerminal() {
        return this == SETTLED || this == REJECTED || this == CANCELLED || 
               this == REFUNDED || this == SETTLEMENT_FAILED;
    }
    
    /**
     * Check if transaction can be processed further
     */
    public boolean isActive() {
        return this == PENDING || this == VALIDATING || this == VALIDATED || this == SETTLING;
    }
}
