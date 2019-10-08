package com.example.wereadv10.ui.clubs.oneClub.events;

public class Event {

    private String event_name;
    private String event_location;
    private String event_date;
    private String event_time;
    private String event_desc;

    public Event (){}

    public Event(String event_name, String event_location, String event_date, String event_time, String event_desc) {
        this.event_name = event_name;
        this.event_location = event_location;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_desc = event_desc;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }
}