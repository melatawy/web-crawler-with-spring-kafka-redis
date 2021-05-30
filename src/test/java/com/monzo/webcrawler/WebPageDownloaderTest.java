package com.monzo.webcrawler;

import com.monzo.webcrawler.redis.WebPageContent;
import com.monzo.webcrawler.redis.WebPageContentArgumentMatcher;
import com.monzo.webcrawler.redis.WebPageContentRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.mockito.Mockito.*;

class WebPageDownloaderTest {

    private KafkaTemplate<String, String> kafkaTemplate;
    private WebPageContentRepo webPageContentRepo;
    private PageDownloader pageDownloader;

    private WebPageDownloader webPageDownloader;

    private String url = "http://monzo.com/about";
    private String baseUrl = "http://monzo.com/";

    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        webPageContentRepo = mock(WebPageContentRepo.class);
        pageDownloader = mock(PageDownloader.class);
        webPageDownloader = new WebPageDownloader(webPageContentRepo, kafkaTemplate, pageDownloader);
        ReflectionTestUtils.setField(webPageDownloader, "webPagesTopicName", "webPagesForTest");
    }

    @Test
    void shouldDownloadUrlAndSendToKafka() throws IOException, InterruptedException {
        String pageContent = "SOME_PAGE_CONTENT";
        WebPageContent webPageContent = WebPageContent.builder().url(url).build();
        when(webPageContentRepo.existsById(eq(url))).thenReturn(false);
        when(webPageContentRepo.save(any())).thenReturn(webPageContent);
        when(pageDownloader.download(eq(url))).thenReturn(pageContent);

        webPageDownloader.download(baseUrl, url);
        verify(pageDownloader).download(eq(url));
        verify(webPageContentRepo).save(argThat(new WebPageContentArgumentMatcher(webPageContent)));
        webPageContent.setPageContent(pageContent);
        verify(webPageContentRepo).save(argThat(new WebPageContentArgumentMatcher(webPageContent)));
        verify(kafkaTemplate).send(eq("webPagesForTest"), eq(url), eq(baseUrl));
    }

    @Test
    void shouldSkipIfExistInRedis() throws IOException, InterruptedException {
        when(webPageContentRepo.existsById(eq(url))).thenReturn(true);

        webPageDownloader.download(baseUrl, url);
        verify(pageDownloader, never()).download(any());
        verify(webPageContentRepo, never()).save(any());
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    void shouldThrowIfDownloadingThrewIOException() throws IOException, InterruptedException {
        when(pageDownloader.download(eq("http://monzo.com/about"))).thenThrow(new IOException());
        shouldThrowIfDownloadingThrewAnException(new IOException());
    }

    @Test
    void shouldThrowIfDownloadingThrewInterruptedException() throws IOException, InterruptedException {
        when(pageDownloader.download(eq("http://monzo.com/about"))).thenThrow(new InterruptedException());
        shouldThrowIfDownloadingThrewAnException(new InterruptedException());
    }

    void shouldThrowIfDownloadingThrewAnException(Exception exception) throws IOException, InterruptedException {

        WebPageContent webPageContent = WebPageContent.builder().url(url).build();
        when(webPageContentRepo.existsById(eq(url))).thenReturn(false);
        when(webPageContentRepo.save(any())).thenReturn(webPageContent);
        when(pageDownloader.download(eq(url))).thenThrow(exception);
        Assertions.assertThrows(exception.getClass(), () -> webPageDownloader.download(baseUrl, url));

        verify(webPageContentRepo, times(1)).save(argThat(new WebPageContentArgumentMatcher(webPageContent)));
        verify(webPageContentRepo, times(1)).delete(argThat(new WebPageContentArgumentMatcher(webPageContent)));
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }
}