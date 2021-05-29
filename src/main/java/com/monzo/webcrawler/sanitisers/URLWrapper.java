package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

class URLWrapper implements IURLWrapper {
    public URLObject url;

    public URLWrapper(URLObject url) {
        this.url = url;
    }

    @Override
    public URLObject sanitise() throws MalformedURLException {
        this.url.setSanitzedUrl(this.url.getRawUrl());
        return this.url;
    }
}
