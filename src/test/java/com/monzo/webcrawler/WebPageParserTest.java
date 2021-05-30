package com.monzo.webcrawler;

import com.monzo.webcrawler.mockhtmls.MockHtmlResponses;
import com.monzo.webcrawler.redis.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

class WebPageParserTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private WebPageContentRepo webPageContentRepo;
    private WebCrawlerOutputRepo webCrawlerOutputRepo;

    WebPageParser webPageParser;

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        webPageContentRepo = mock(WebPageContentRepo.class);
        webCrawlerOutputRepo = mock(WebCrawlerOutputRepo.class);
        webPageParser = new WebPageParser(webPageContentRepo, webCrawlerOutputRepo, kafkaTemplate);
        ReflectionTestUtils.setField(webPageParser, "urlsTopicName", "urlsForTest");
    }

    @Test
    void shouldParsePageAndExtractCorrectAnchorsAndSendToKafka() {
        String url = "https://monzo.com/about";
        String baseUrl = "https://monzo.com/";
        String pageContent = MockHtmlResponses.allPossibleAnchors();
        String[] childUrls = {
                "https://monzo.com/home",
                "https://monzo.com/relative",
                "https://facebook.com/external",
                "https://community.monzo.com/forum"
        };
        WebPageContent webPageContent = WebPageContent.builder().url(url).pageContent(pageContent).build();
        when(webPageContentRepo.findById(eq(url))).thenReturn(Optional.of(webPageContent));
        when(webPageContentRepo.existsById(anyString())).thenReturn(false);
        WebCrawlerOutput outputOfTheParse = WebCrawlerOutput
                .builder()
                .baseUrl(baseUrl)
                .url(url)
                .children(new HashSet<>(Arrays.asList(childUrls[0], childUrls[1])))
                .build();

        webPageParser.parse(baseUrl, url);

        verify(webPageContentRepo).findById(eq(url));
        verify(webPageContentRepo, times(2)).existsById(anyString());
        verify(webPageContentRepo).existsById(eq(childUrls[0]));
        verify(webPageContentRepo).existsById(eq(childUrls[1]));
        verify(kafkaTemplate, times(2)).send(eq("urlsForTest"), anyString(), eq(baseUrl));
        verify(kafkaTemplate).send(eq("urlsForTest"), eq(childUrls[0]), eq(baseUrl));
        verify(kafkaTemplate).send(eq("urlsForTest"), eq(childUrls[1]), eq(baseUrl));
        verify(webCrawlerOutputRepo).save(argThat(new WebCrawlerOutputArgumentMatcher(outputOfTheParse)));

    }

    @Test
    void shouldSkipParsingPageIfPageContentDoesNotExist() {
        String url = "https://monzo.com/about";
        String baseUrl = "https://monzo.com/";
        when(webPageContentRepo.findById(eq(url))).thenReturn(Optional.empty());

        webPageParser.parse(baseUrl, url);

        verify(webPageContentRepo).findById(eq(url));
        verify(webPageContentRepo, never()).existsById(anyString());
        verify(kafkaTemplate, never()).send(anyString(), anyString(), eq(baseUrl));
        verify(webCrawlerOutputRepo, never()).save(any());

    }

}