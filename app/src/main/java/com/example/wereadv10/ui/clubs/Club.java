package com.example.wereadv10.ui.clubs;

public class Club {

    private long ID;
    private String club_name;
    private String club_owner;
    private String club_description;
    private String club_image;


    public  Club() { }

    public Club(long ID, String club_name, String club_owner, String club_description, String club_image) {
        this.ID = ID;
        this.club_name = club_name;
        this.club_owner = club_owner;
        this.club_description = club_description;
        this.club_image = club_image;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getClub_name() {
        return club_name;
    }

    public void setClub_name(String club_name) {
        this.club_name = club_name;
    }

    public String getClub_owner() {
        return club_owner;
    }

    public void setClub_owner(String club_owner) {
        this.club_owner = club_owner;
    }

    public String getClub_description() {
        return club_description;
    }

    public void setClub_description(String club_description) {
        this.club_description = club_description;
    }

    public String getClub_image() {
        return club_image;
    }

    public void setClub_image(String club_image) {
        this.club_image = club_image;
    }


}
