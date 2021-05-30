package com.monzo.webcrawler.redis;

import org.mockito.ArgumentMatcher;

public class WebPageContentArgumentMatcher implements ArgumentMatcher<WebPageContent> {
    private WebPageContent webPageContentToMatch;

    public WebPageContentArgumentMatcher(WebPageContent webPageContentToMatch) {
        this.webPageContentToMatch = webPageContentToMatch;
    }

    @Override
    public boolean matches(WebPageContent webPageContent) {
        return webPageContent.getUrl().equals(webPageContentToMatch.getUrl()) &&
            (
                (webPageContent.getPageContent() == null && webPageContentToMatch.getPageContent() == null)
                ||
                (webPageContent.getPageContent() != null && webPageContent.getPageContent().equals(webPageContentToMatch.getPageContent()))
            );
    }
}
