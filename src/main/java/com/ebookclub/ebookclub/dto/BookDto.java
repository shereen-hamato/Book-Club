package com.ebookclub.ebookclub.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class BookDto {


    private String title;

    private String author;

    @Size(max=500)
    private String overview;

    private String isdn;

    private String imageUrl;

    @NotNull
    private Integer genreId;

    public BookDto(String title, String author, @Size(max = 500) String overview, String isdn, String imageUrl, @NotNull Integer genreId) {
        this.title = title;
        this.author = author;
        this.overview = overview;
        this.isdn = isdn;
        this.imageUrl = imageUrl;
        this.genreId = genreId;
    }

    public BookDto(){
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }
}
