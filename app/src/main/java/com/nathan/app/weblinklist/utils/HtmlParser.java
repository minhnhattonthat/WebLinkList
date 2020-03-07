package com.nathan.app.weblinklist.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HtmlParser {

    public static String getPageTitle(String body) {
        String title = "";
        Pattern pattern = Pattern.compile("<title>.*</title>");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            title = body.substring(matcher.start(), matcher.end());
            title = title.replace("<title>", "").replace("</title>", "");
        }
        return title;
    }

    public static String getImageUrl(String baseUrl, String body) {
        String imageUrl = "";
        Pattern pattern = Pattern.compile("(<img.*src|content)=\"(https?:/)?/.*\\.(jpg|png)");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            imageUrl = body.substring(matcher.start(), matcher.end());
            imageUrl = imageUrl.replaceFirst("(<img.*src|content)=\"", "");
            if (!imageUrl.startsWith("http")) {
                if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
                imageUrl = baseUrl + imageUrl;
            } else if (imageUrl.indexOf("http") != imageUrl.lastIndexOf("http")) {
                imageUrl = imageUrl.substring(imageUrl.lastIndexOf("http"));
            }
        }
        return imageUrl;
    }
}
