package com.luxoft.homework.module3.task2;


import java.util.Objects;

public class Book {

    private String name;
    private String author;
    private String rating;
    private String reviews;
    private Integer price;
    private Integer year;
    private String genre;

    public Book() {
    }

    public Book(String row) {
        String[] sp = row.split(";");
        this.name = sp[0];
        this.author = sp[1];
        this.rating = sp[2];
        this.reviews = sp[3];
        this.price = Integer.getInteger(sp[4]);
        this.year = Integer.getInteger(sp[5]);
        this.genre = sp[6];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(name, book.name) && Objects.equals(author, book.author) && Objects.equals(rating, book.rating) && Objects.equals(reviews, book.reviews) && Objects.equals(price, book.price) && Objects.equals(year, book.year) && Objects.equals(genre, book.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, author, rating, reviews, price, year, genre);
    }
}
