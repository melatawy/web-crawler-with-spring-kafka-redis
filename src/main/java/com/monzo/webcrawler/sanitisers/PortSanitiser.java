package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

class PortSanitiser extends AbstractSanitiser {

    public PortSanitiser(URLWrapper urlWrapper) {
        super(urlWrapper);
    }

    @Override
    public URLObject sanitise() throws MalformedURLException {
        URLObject urlObject = super.sanitise();
        if (urlObject.getUrl().getPort() == 80) {
            urlObject.setSanitzedUrl(urlObject.getSanitzedUrl().replaceFirst(":80", ""));
        }
        return urlObject;
    }
}
