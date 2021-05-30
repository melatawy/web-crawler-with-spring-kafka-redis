package com.monzo.webcrawler;

import com.monzo.webcrawler.redis.WebCrawlerOutputRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class WebCrawlerLightApplicationTests {
    private KafkaTemplate<String, String> kafkaTemplate;
    private WebCrawlerOutputRepo webCrawlerOutputRepo;

    private WebCrawlerApplication application;

    @BeforeEach
    public void setUp(){
        kafkaTemplate = mock(KafkaTemplate.class);
        webCrawlerOutputRepo = mock(WebCrawlerOutputRepo.class);
        application = new WebCrawlerApplication(kafkaTemplate, webCrawlerOutputRepo);
        ReflectionTestUtils.setField(application, "urlsTopicName", "urlsForTest");
    }

    @Test
    void crawlExtractBaseUrlSuccessfully() throws MalformedURLException {
        application.crawl("http://monzo.com/about");

        verify(kafkaTemplate).send(eq("urlsForTest"), eq("http://monzo.com/about"), eq("http://monzo.com/"));
    }

    @Test
    void crawlPrepareBaseUrlWithAnEndingSlash() throws MalformedURLException {
        application.crawl("http://monzo.com");

        verify(kafkaTemplate).send(eq("urlsForTest"), eq("http://monzo.com/"), eq("http://monzo.com/"));
    }

    @Test
    void crawlShouldFailIfReceivedMalformedURL() throws MalformedURLException {
        assertThrows(MalformedURLException.class, () -> application.crawl("NOT_A_URL"));
    }

}
