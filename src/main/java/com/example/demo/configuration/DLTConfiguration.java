package com.example.demo.configuration;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class DLTConfiguration {

    @Bean
    public CommonErrorHandler errorHandler(KafkaOperations<String, Object> template) {
        return new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(template, (record, ex) -> new TopicPartition("genericDLT", -1)), new FixedBackOff(1000L, 2));
    }

}
