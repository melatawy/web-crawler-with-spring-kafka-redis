package com.monzo.webcrawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class WebCrawlerApplicationTests {

    @Autowired
    private WebCrawlerApplication application;

    @SpyBean
    private KafkaTemplate<String, String> kafkaTemplate;

//    private WebCrawlerApplication application;

    @BeforeEach
    public void setUp(){
//        kafkaTemplate = mock(KafkaTemplate.class);
//        application = new WebCrawlerApplication(kafkaTemplate, webCrawlerOutputRepo);
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(application).isNotNull();
    }

    @Test
    void crawlShouldSendCorrectInitialUrlAndBaseUrlToKafka() throws MalformedURLException {
        String response = application.crawl("http://monzo.com/about");

        verify(kafkaTemplate).send("test-urls", "http://monzo.com/about", "http://monzo.com/");
        assertEquals(response, "Crawling: http://monzo.com/about");
    }

}
