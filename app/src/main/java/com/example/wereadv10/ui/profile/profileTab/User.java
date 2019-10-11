package com.example.wereadv10.ui.profile.profileTab;

public class User {

    private String email;
    private String name;
    private String id;
    private int image;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() { }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
