package com.monzo.webcrawler.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@EnableConfigurationProperties
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${urls.topic.group.id}")
    private String urlsGroupId;

    @Value(value = "${webPages.topic.group.id}")
    private String webPagesGroupId;

    @Value(value = "${urls.listener.concurrency}")
    private int urlsConsumerConcurrency;

    @Value(value = "${webPages.listener.concurrency}")
    private int webPagesConsumerConcurrency;

    @Bean
    public ConsumerFactory<String, String> urlsConsumerFactory() {
        Map<String, Object> props = baseConsumerFactoryProps();
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                urlsGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConsumerFactory<String, String> webPagesConsumerFactory() {
        Map<String, Object> props = baseConsumerFactoryProps();
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                webPagesGroupId);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> urlsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(urlsConsumerFactory());
        factory.setConcurrency(urlsConsumerConcurrency);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> webPagesKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(webPagesConsumerFactory());
        factory.setConcurrency(webPagesConsumerConcurrency);
        return factory;
    }

    private Map<String, Object> baseConsumerFactoryProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return props;
    }
}