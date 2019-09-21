package com.example.wereadv10;

public class Category {
    private String category_name;

    public Category(){}
    public Category(String id , String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
