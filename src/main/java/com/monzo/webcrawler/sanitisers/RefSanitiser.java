package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

class RefSanitiser extends AbstractSanitiser {

    public RefSanitiser(IURLWrapper urlWrapper) {
        super(urlWrapper);
    }

    @Override
    public URLObject sanitise() throws MalformedURLException {
        URLObject urlObject = super.sanitise();
        String externalFormOfURL = urlObject.getUrl().toExternalForm();
        if (urlObject.getUrl().getRef() != null) {
            urlObject.setSanitzedUrl(externalFormOfURL.replace(urlObject.getUrl().getRef(), ""));
            urlObject.setSanitzedUrl(urlObject.getSanitzedUrl().replace("#", ""));
        }

        return urlObject;
    }
}
