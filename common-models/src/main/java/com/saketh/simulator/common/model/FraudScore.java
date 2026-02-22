package com.saketh.simulator.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Fraud detection result from ML/rule-based validation.
 * Contains detailed analysis and risk indicators.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudScore {

    /**
     * Associated transaction ID
     */
    private String transactionId;

    /**
     * Overall fraud probability (0.0 = safe, 1.0 = definitely fraud)
     */
    private Double fraudProbability;

    /**
     * Risk level classification
     */
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL

    /**
     * Rule-based score (0-100)
     */
    private Integer ruleBasedScore;

    /**
     * ML model score (0.0-1.0)
     */
    private Double mlScore;

    /**
     * Model version used for scoring
     */
    private String modelVersion;

    /**
     * List of triggered fraud rules
     */
    private List<String> triggeredRules;

    /**
     * Anomaly indicators detected
     */
    private List<String> anomalies;

    /**
     * Recommendation: APPROVE, REVIEW, REJECT
     */
    private String recommendation;

    /**
     * Human-readable explanation
     */
    private String explanation;

    /**
     * Confidence level of the prediction (0.0-1.0)
     */
    private Double confidence;

    /**
     * Time taken for fraud analysis (milliseconds)
     */
    private Long processingTimeMs;

    /**
     * Timestamp when analysis was performed
     */
    @Builder.Default
    private LocalDateTime analyzedAt = LocalDateTime.now();

    /**
     * Whether manual review is required
     */
    @Builder.Default
    private Boolean requiresManualReview = false;
}
