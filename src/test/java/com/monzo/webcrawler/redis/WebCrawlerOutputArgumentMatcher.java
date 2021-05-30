package com.monzo.webcrawler.redis;

import org.mockito.ArgumentMatcher;

public class WebCrawlerOutputArgumentMatcher implements ArgumentMatcher<WebCrawlerOutput> {
    private WebCrawlerOutput webCrawlerOutputToMatch;

    public WebCrawlerOutputArgumentMatcher(WebCrawlerOutput webCrawlerOutputToMatch) {
        this.webCrawlerOutputToMatch = webCrawlerOutputToMatch;
    }

    @Override
    public boolean matches(WebCrawlerOutput webCrawlerOutput) {
        return webCrawlerOutput.getUrl().equals(webCrawlerOutputToMatch.getUrl()) &&
                webCrawlerOutput.getBaseUrl().equals(webCrawlerOutputToMatch.getBaseUrl()) &&
                (
                    (webCrawlerOutput.getChildren() == null && webCrawlerOutputToMatch.getChildren() == null)
                    ||
                    (webCrawlerOutput.getChildren() != null && webCrawlerOutput.getChildren().equals(webCrawlerOutputToMatch.getChildren()))
                );
    }
}
