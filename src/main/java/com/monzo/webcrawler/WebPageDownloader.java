package com.monzo.webcrawler;

import com.monzo.webcrawler.redis.WebPageContent;
import com.monzo.webcrawler.redis.WebPageContentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@EnableConfigurationProperties
public class WebPageDownloader {
    private final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    @Value(value = "${webPages.topic.name}")
    private String webPagesTopicName;

    private WebPageContentRepo webPageContentRepo;
    private KafkaTemplate<String, String> kafkaTemplate;
    private PageDownloader pageDownloader;

    @Autowired
    public WebPageDownloader(WebPageContentRepo webPageContentRepo, KafkaTemplate<String, String> kafkaTemplate, PageDownloader pageDownloader) {
        this.webPageContentRepo = webPageContentRepo;
        this.kafkaTemplate = kafkaTemplate;
        this.pageDownloader = pageDownloader;
    }

    @KafkaListener(topics = "${urls.topic.name}", groupId = "${urls.topic.group.id}", containerFactory = "urlsKafkaListenerContainerFactory")
    public void download(@Payload String baseUrl, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String url) throws IOException, InterruptedException {
        if (webPageContentRepo.existsById(url)) return;
        WebPageContent webPageContent = webPageContentRepo.save(WebPageContent.builder().url(url).build());
        String pageBody = "";
        try {
            pageBody = pageDownloader.download(url);
        } catch (IOException | InterruptedException e) {
            logger.error("Failed to download URL "+url);
            webPageContentRepo.delete(webPageContent);
            throw e;
        }
        logger.info(url+" WILL BE SAVED AFTER DOWNLOADED NOW");
        webPageContent.setPageContent(pageBody);
        webPageContentRepo.save(webPageContent);
        kafkaTemplate.send(webPagesTopicName, url, baseUrl); // Sending URL as key. Base URL in the message
    }
}
