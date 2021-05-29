package com.monzo.webcrawler.sanitisers;

import java.net.MalformedURLException;

interface IURLWrapper {
    URLObject sanitise() throws MalformedURLException;
}
