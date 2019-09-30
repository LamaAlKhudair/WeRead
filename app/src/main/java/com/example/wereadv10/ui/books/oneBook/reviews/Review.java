package com.example.wereadv10.ui.books.oneBook.reviews;

class Review {
    public Review() {
    }

    private String userName; // String for now
    private String text;
    private String revTitle;
//    rate here


    public Review(String userName, String revTitle, String text) {
        this.userName = userName;
        this.revTitle = revTitle;
        this.text = text;
    }

    public String getRevTitle() {
        return revTitle;
    }

    public void setRevTitle(String revTitle) {
        this.revTitle = revTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getText() {
        return text;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setText(String text) {
        this.text = text;
    }
}
