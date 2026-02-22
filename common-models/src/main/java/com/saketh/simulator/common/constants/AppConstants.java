package com.saketh.simulator.common.constants;

/**
 * Application-wide configuration constants.
 */
public final class AppConstants {

    private AppConstants() {
        // Utility class, prevent instantiation
    }

    // Fraud Detection Thresholds
    public static final double FRAUD_SCORE_THRESHOLD_HIGH = 0.75;
    public static final double FRAUD_SCORE_THRESHOLD_MEDIUM = 0.50;
    public static final double FRAUD_SCORE_THRESHOLD_LOW = 0.25;

    // Transaction Limits
    public static final String MAX_TRANSACTION_AMOUNT = "100000.00";
    public static final String MIN_TRANSACTION_AMOUNT = "0.01";
    public static final int MAX_DAILY_TRANSACTIONS = 50;

    // Risk Levels
    public static final String RISK_LOW = "LOW";
    public static final String RISK_MEDIUM = "MEDIUM";
    public static final String RISK_HIGH = "HIGH";
    public static final String RISK_CRITICAL = "CRITICAL";

    // Default Values
    public static final String DEFAULT_CURRENCY = "USD";
    public static final String DEFAULT_COUNTRY = "US";
    public static final String DEFAULT_STATUS = "PENDING";

    // Kafka Configuration
    public static final int KAFKA_PARTITION_COUNT = 3;
    public static final short KAFKA_REPLICATION_FACTOR = 1;
    public static final int KAFKA_CONSUMER_THREADS = 3;

    // API Timeouts (milliseconds)
    public static final int ML_API_TIMEOUT = 5000;
    public static final int DATABASE_TIMEOUT = 3000;
    public static final int API_RESPONSE_TIMEOUT = 10000;

    // Retry Configuration
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final long RETRY_BACKOFF_MS = 1000;

    // Batch Processing
    public static final int BATCH_SIZE = 100;
    public static final int BATCH_TIMEOUT_MS = 5000;
}
