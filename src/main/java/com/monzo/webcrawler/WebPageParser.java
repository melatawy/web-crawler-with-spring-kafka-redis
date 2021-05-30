package com.monzo.webcrawler;

import com.monzo.webcrawler.redis.WebCrawlerOutput;
import com.monzo.webcrawler.redis.WebCrawlerOutputRepo;
import com.monzo.webcrawler.redis.WebPageContent;
import com.monzo.webcrawler.redis.WebPageContentRepo;
import com.monzo.webcrawler.sanitisers.URLObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@EnableConfigurationProperties
public class WebPageParser {
    private final Logger logger = LoggerFactory.getLogger(KafkaListener.class);

    @Value(value = "${urls.topic.name}")
    private String urlsTopicName;

    private WebPageContentRepo webPageContentRepo;
    private WebCrawlerOutputRepo webCrawlerOutputRepo;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public WebPageParser(WebPageContentRepo webPageContentRepo,
                         WebCrawlerOutputRepo webCrawlerOutputRepo,
                         KafkaTemplate<String, String> kafkaTemplate) {
        this.webPageContentRepo = webPageContentRepo;
        this.webCrawlerOutputRepo = webCrawlerOutputRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${webPages.topic.name}", groupId = "${webPages.topic.group.id}", containerFactory = "webPagesKafkaListenerContainerFactory")
    public void parse(@Payload String baseUrl, @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String url) {
        Optional<WebPageContent> webPageContentToParse = webPageContentRepo.findById(url);
        String document = webPageContentToParse
                .orElse(WebPageContent
                        .builder()
                        .url("URL_NOT_FOUND")
                        .build())
                .getPageContent();
        if (document == null || document.isBlank()) {
            logger.warn(url+" was not found in web page repo");
            return;
        }
        Document doc = Jsoup.parse(document);
        doc.setBaseUri(baseUrl);
        Elements anchors = doc.select("a");
        Set<String> foundUrls = new HashSet<>();
        for (Element anchor : anchors) {
            String absoluteUrl = anchor.absUrl("href");
            try {
                absoluteUrl = new URLObject(absoluteUrl).getSanitzedUrl();
            } catch (MalformedURLException e) {
                continue;
            }
            if (absoluteUrl.startsWith(baseUrl)) {
                foundUrls.add(absoluteUrl);
                if (!webPageContentRepo.existsById(absoluteUrl)) {
                    kafkaTemplate.send(urlsTopicName, absoluteUrl, baseUrl);
                }
            }
        }
        webCrawlerOutputRepo.save(WebCrawlerOutput.builder()
                .url(url)
                .baseUrl(baseUrl)
                .children(foundUrls)
                .build());
        logger.info("START ==========================");
        logger.info(url);
        for (String foundUrl : foundUrls) {
            logger.info("\t" + foundUrl);
        }
        logger.info("END ==========================");
    }
}
