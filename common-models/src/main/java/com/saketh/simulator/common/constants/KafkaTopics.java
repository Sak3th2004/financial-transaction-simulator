package com.saketh.simulator.common.constants;

/**
 * Kafka topic names used across all microservices.
 * Centralized configuration to avoid magic strings.
 */
public final class KafkaTopics {

    private KafkaTopics() {
        // Utility class, prevent instantiation
    }

    /**
     * Topic for raw incoming transactions from ingestion service
     */
    public static final String RAW_TRANSACTIONS = "raw-transactions";

    /**
     * Topic for validated transactions ready for settlement
     */
    public static final String VALIDATED_TRANSACTIONS = "validated-transactions";

    /**
     * Topic for transactions that failed validation
     */
    public static final String FAILED_TRANSACTIONS = "failed-transactions";

    /**
     * Topic for transactions flagged as potentially fraudulent
     */
    public static final String FRAUD_ALERTS = "fraud-alerts";

    /**
     * Topic for settled transactions
     */
    public static final String SETTLED_TRANSACTIONS = "settled-transactions";

    /**
     * Topic for settlement failures
     */
    public static final String SETTLEMENT_FAILURES = "settlement-failures";

    /**
     * Topic for dead letter queue (processing errors)
     */
    public static final String DLQ_TRANSACTIONS = "dlq-transactions";

    /**
     * Topic for audit events (for reporting/analytics)
     */
    public static final String AUDIT_EVENTS = "audit-events";

    /**
     * Topic for metrics and monitoring data
     */
    public static final String METRICS = "transaction-metrics";
}
