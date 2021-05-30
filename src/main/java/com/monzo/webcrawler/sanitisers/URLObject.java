package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;
import java.net.URL;

public class URLObject {
    private String rawUrl;
    private String sanitzedUrl;
    private URL url;

    public URLObject(String rawUrl) throws MalformedURLException {
        this.rawUrl = rawUrl;
        this.url = new URL(rawUrl);
        this.sanitzedUrl = new URLSanitser().sanitise(this);
        this.url = new URL(this.sanitzedUrl);
    }

    public static String getBaseUrl(String rawUrl) throws MalformedURLException {
        URL url = new URL(rawUrl);
        return url.getProtocol() + "://" + url.getHost();
    }

    public String getRawUrl() {
        return this.rawUrl;
    }

    public String getSanitzedUrl() {
        return sanitzedUrl;
    }

    public void setSanitzedUrl(String sanitzedUrl) throws MalformedURLException {
        this.sanitzedUrl = sanitzedUrl;
        this.url = new URL(this.sanitzedUrl);
    }

    public URL getUrl() {
        return url;
    }
}
