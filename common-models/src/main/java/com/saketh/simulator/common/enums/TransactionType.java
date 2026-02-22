package com.saketh.simulator.common.enums;

/**
 * Enumeration of supported transaction types.
 */
public enum TransactionType {
    
    /**
     * Money added to account
     */
    DEPOSIT,
    
    /**
     * Money withdrawn from account
     */
    WITHDRAWAL,
    
    /**
     * Transfer between accounts
     */
    TRANSFER,
    
    /**
     * Payment to merchant/vendor
     */
    PAYMENT,
    
    /**
     * Refund from merchant
     */
    REFUND,
    
    /**
     * Fee charged by the system
     */
    FEE,
    
    /**
     * Interest credited
     */
    INTEREST,
    
    /**
     * Adjustment/correction
     */
    ADJUSTMENT;
    
    /**
     * Check if transaction type is debit
     */
    public boolean isDebit() {
        return this == WITHDRAWAL || this == TRANSFER || this == PAYMENT || this == FEE;
    }
    
    /**
     * Check if transaction type is credit
     */
    public boolean isCredit() {
        return this == DEPOSIT || this == REFUND || this == INTEREST;
    }
}
