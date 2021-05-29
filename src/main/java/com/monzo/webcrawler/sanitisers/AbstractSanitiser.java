package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

abstract class AbstractSanitiser implements IURLWrapper {
    private IURLWrapper urlWrapper;

    public AbstractSanitiser(IURLWrapper urlWrapper) {
        this.urlWrapper = urlWrapper;
    }

    public URLObject sanitise() throws MalformedURLException {
        return urlWrapper.sanitise();
    }
}
