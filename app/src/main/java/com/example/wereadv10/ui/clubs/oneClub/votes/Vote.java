package com.example.wereadv10.ui.clubs.oneClub.votes;

public class Vote {

    private String option1;
    private String option2;
    private String vote_desc;
    private String vote_title;
    //counters?

    public Vote(){}

    public Vote(String option1, String option2, String vote_desc, String vote_title) {
        this.option1 = option1;
        this.option2 = option2;
        this.vote_desc = vote_desc;
        this.vote_title = vote_title;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getVote_desc() {
        return vote_desc;
    }

    public void setVote_desc(String vote_desc) {
        this.vote_desc = vote_desc;
    }

    public String getVote_title() {
        return vote_title;
    }

    public void setVote_title(String vote_title) {
        this.vote_title = vote_title;
    }
}
