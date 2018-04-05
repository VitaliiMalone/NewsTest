package com.example.user.newstest;

public class News {
    private String sourceName;
    private String title;
    private String description;
    private String url;
    private String imageUrl;

    public News(String sourceName, String title, String description, String url, String imageUrl) {
        this.sourceName = sourceName;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
