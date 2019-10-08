package com.example.wereadv10.ui.books;

import com.example.wereadv10.ui.categories.Category;

public class Book {

    private String ID;
    private String author;
    private Category book_category;
    private String book_title;
    private String summary;
    private String cover;

    public long getRate() {
        return rate;
    }

    public void setRate(long rate) {
        this.rate = rate;
    }

    private long rate;


    public Book() { }


    public Book(String author, Category book_category, String book_title, String summary) {
        this.ID = ID;
        this.author = author;
        this.book_category = book_category;
        this.book_title = book_title;
        this.summary = summary;
    }



    public String getID() {
        return ID;
    }

    public String getAuthor() {
        return author;
    }

    public Category getBook_category() {
        return book_category;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getSummary() {
        return summary;
    }

    public String getCover() {
        return cover;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBook_category(Category book_category) {
        this.book_category = book_category;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}



