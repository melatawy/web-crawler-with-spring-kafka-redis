package com.monzo.webcrawler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.mockito.Mockito.*;

public class PageDownloaderTest {

    @Test
    public void download() throws IOException, InterruptedException {
        PageDownloader pageDownloader = new PageDownloader();
        HttpClient mockedClient = mock(HttpClient.class);
        HttpResponse<Object> someHttpResponse = mock(HttpResponse.class);
        when(someHttpResponse.body()).thenReturn("someResponse");
        when(mockedClient.send(any(), any())).thenReturn(someHttpResponse);

        try(MockedStatic<HttpClient> staticMockedHttpClient = mockStatic(HttpClient.class)){
            staticMockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockedClient);

            String body = pageDownloader.download("https://monzo.com");

            Assertions.assertEquals(body, "someResponse");
        }
    }
}