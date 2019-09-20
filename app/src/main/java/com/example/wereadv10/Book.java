package com.example.wereadv10;

public class Book {

    private long ID;
    private String author;
    private String book_category;
    private String book_title;
    private String summary;
    private int cover;


    public Book(String author, String book_category, String book_title, String summary) {
        this.author = author;
        this.book_category = book_category;
        this.book_title = book_title;
        this.summary = summary;
    }



    public long getID() {
        return ID;
    }

    public String getAuthor() {
        return author;
    }

    public String getBook_category() {
        return book_category;
    }

    public String getBook_title() {
        return book_title;
    }

    public String getSummary() {
        return summary;
    }

    public int getCover() {
        return cover;
    }


    public void setID(long ID) {
        this.ID = ID;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }
}



