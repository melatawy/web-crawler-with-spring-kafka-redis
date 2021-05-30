package com.monzo.webcrawler.mockhtmls;

public class MockHtmlResponses {
    public static String plainText() {
        return "HERE ARE SOME RANDOM CHARACTERS";
    }

    public static String allPossibleAnchors() {
        return "HERE ARE SOME RANDOM CHARACTERS"
                + " <a href='https://monzo.com/home'>Monzo Home</a>"
                + " <a href='tel://0987654321'>Monzo Contact</a>"
                + " <a href='/relative'>Monzo Relative</a>"
                + " <a href='https://facebook.com/external'>Facebook</a>"
                + " <a href='https://community.monzo.com/forum'>Subdomain</a>";
    }

    public static String oneValidAndoneInvalidAnchors() {
        return "HERE ARE SOME RANDOM CHARACTERS <a href='https://monzo.com/home'>Monzo Home</a> " +
                "And <a href='tel:0123456789'>Contact Monzo</a>";
    }
}
