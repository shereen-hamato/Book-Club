package com.ebookclub.ebookclub.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String author;

    @Column(length = 2000)
    private String overview;

    @Column(unique = true)
    private String isdn;

    @Column(name ="image_url",length = 500)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="book_genre")
    private Genre bookGenre;

    @ManyToOne
    @JoinColumn(name="book_owner", updatable = false)
    private User bookOwner;

    @OneToMany(mappedBy = "book")
    Set<Rating> ratings;

    public Book(String title, String author, String overview, String isdn, String imageUrl, Genre bookGenre, User bookOwner) {
        this.title = title;
        this.author = author;
        this.overview = overview;
        this.isdn = isdn;
        this.imageUrl = imageUrl;
        this.bookGenre = bookGenre;
        this.bookOwner = bookOwner;
    }

    public Book(){

    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Genre getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(Genre bookGenre) {
        this.bookGenre = bookGenre;
    }

    public User getBookOwner() {
        return bookOwner;
    }

    public void setBookOwner(User bookOwner) {
        this.bookOwner = bookOwner;
    }

    public Set<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<Rating> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", overview='" + overview + '\'' +
                ", isdn='" + isdn + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", bookGenre=" + bookGenre.getId() +
                ", bookOwner=" + bookOwner.getId() +
                ", ratings=" + ratings +
                '}';
    }
}
