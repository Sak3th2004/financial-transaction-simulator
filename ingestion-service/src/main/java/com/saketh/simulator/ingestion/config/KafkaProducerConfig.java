package com.saketh.simulator.ingestion.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.saketh.simulator.common.constants.KafkaTopics;
import com.saketh.simulator.common.model.Transaction;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka producer configuration for publishing transaction events.
 */
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    /**
     * Configure Kafka producer properties
     */
    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        
        // Basic configuration
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        
        // Performance tuning
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // Wait for all replicas
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3); // Retry up to 3 times
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384); // 16KB batch size
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10); // Wait 10ms to batch
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432); // 32MB buffer
        
        // Idempotence for exactly-once semantics
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        
        // Compression for better throughput
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * KafkaTemplate for publishing messages
     */
    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Create raw-transactions topic if it doesn't exist
     */
    @Bean
    public NewTopic rawTransactionsTopic() {
        return new NewTopic(KafkaTopics.RAW_TRANSACTIONS, 3, (short) 1);
    }

    /**
     * Create audit-events topic for tracking
     */
    @Bean
    public NewTopic auditEventsTopic() {
        return new NewTopic(KafkaTopics.AUDIT_EVENTS, 3, (short) 1);
    }
}
