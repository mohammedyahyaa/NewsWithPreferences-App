package com.example.mohamed.newswithpreferences;

public class Article {

    private String section;
    private String title;
    private String url;
    private String date;
    private String authorName;

    public Article(String section, String title, String date, String url , String authorName) {
        this.setSection(section);
        this.setTitle(title);
        this.setUrl(url);
        this.setDate(date);
        this.setAuthorName(authorName);

    }


    public String getSection() {
        return section;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
