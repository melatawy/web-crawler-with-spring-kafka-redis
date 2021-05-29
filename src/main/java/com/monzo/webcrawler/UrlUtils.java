package com.monzo.webcrawler;

public class UrlUtils {
//    public static String getBaseUrl(String url) throws MalformedURLException {
//        URL urlObject = new URL(url);
//        String baseUrl = urlObject.getProtocol() + "://" +urlObject.getHost();
//        if(urlObject.getPort() > -1) {
//            baseUrl = baseUrl + ":" + urlObject.getPort();
//        }
//        if(url.equals(baseUrl)) {
//            url += "/";
//        }
//        baseUrl += "/";
//        return UrlUtils.createMessage(baseUrl, url);
//    }
//
//    public static String createMessage(String baseUrl, String url) {
//        return baseUrl + "$" + url;
//    }
//
//    public static String getBaseUrlFromMessage(String message) {
//        return message.substring(0, message.indexOf('$'));
//    }
//
//    public static String getUrlFromMessage(String message) {
//        return UrlUtils.getUrlFromMessage(message, UrlUtils.getBaseUrlFromMessage(message));
//    }
//
//    public static String getUrlFromMessage(String message, String baseUrl) {
//        return message.replace(baseUrl+"$", "");
//    }
}
