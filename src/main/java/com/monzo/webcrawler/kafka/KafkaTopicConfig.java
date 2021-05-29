package com.monzo.webcrawler.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class KafkaTopicConfig {

    @Value(value = "${urls.topic.name}")
    private String urlsTopicName;

    @Value(value = "${webPages.topic.name}")
    private String webPagesTopicName;

    @Value(value = "${urls.topic.partitions}")
    private int urlsPartitionsCount;

    @Value(value = "${urls.topic.replications}")
    private short urlsReplicationFactor;

    @Value(value = "${webPages.topic.partitions}")
    private int webPagesPartitionsCount;

    @Value(value = "${webPages.topic.replications}")
    private short webPagesReplicationFactor;

    @Bean
    public NewTopic urls() {
        return new NewTopic(urlsTopicName, urlsPartitionsCount, urlsReplicationFactor);
    }

    @Bean
    public NewTopic webPages() {
        return new NewTopic(webPagesTopicName, webPagesPartitionsCount, webPagesReplicationFactor);
    }
}
