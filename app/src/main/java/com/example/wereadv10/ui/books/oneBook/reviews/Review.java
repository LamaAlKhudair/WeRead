package com.example.wereadv10.ui.books.oneBook.reviews;

class Review {

    private String userName; // String for now
    private String text;
//    rate here


    public Review(String userName, String text) {
        this.userName = userName;
        this.text = text;
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
