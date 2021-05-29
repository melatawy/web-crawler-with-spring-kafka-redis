package com.monzo.webcrawler.configproperties;

import org.springframework.context.annotation.Configuration;

@Configuration

public class UrlsTopicConfigProperties {
    /**
     * Urls Topic Name
     */
    String name;

    /**
     * Urls Topic Partition Count
     */
    int partitions;

    /**
     * Urls Topic Replication Factor
     */
    short replications;
}
