package com.nathan.app.weblinklist.model;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class WebLink implements Comparable<WebLink> {

    private String url;

    private String imgUrl;

    private String title;

    private long created;

    public WebLink(String url, String imgUrl, String title) {
        this.url = url;
        this.imgUrl = imgUrl;
        this.title = title;
        this.created = currentTimeMillis();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreated() {
        return created;
    }

    @Override
    public int compareTo(WebLink o) {
        if (url.equals(o.url)) {
            return (int) (created - o.created);
        }
        return url.compareTo(o.url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebLink webLink = (WebLink) o;

        if (created != webLink.created) return false;
        if (!Objects.equals(url, webLink.url)) return false;
        if (!Objects.equals(imgUrl, webLink.imgUrl)) return false;
        return Objects.equals(title, webLink.title);
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (imgUrl != null ? imgUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) (created ^ (created >>> 32));
        return result;
    }
}
