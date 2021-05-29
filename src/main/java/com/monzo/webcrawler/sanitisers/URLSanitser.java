package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

public class URLSanitser {
    public String sanitise(URLObject urlObject) throws MalformedURLException {
        return new RefSanitiser(new PortSanitiser(new URLWrapper(urlObject))).sanitise().getSanitzedUrl();
    }
}
