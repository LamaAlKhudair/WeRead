package com.example.wereadv10.ui.clubs.oneClub.votes;

public class Vote {

    private String option1;
    private String option2;
    private String vote_desc;
    private String vote_title;
    private String counter_op1;
    private String counter_op2;
    private String vote_id;
    private String counter_tot;

    public Vote(){}

    public Vote(String option1, String option2, String vote_desc, String vote_title, String counter_op1 , String counter_op2, String counter_tot) {
        this.option1 = option1;
        this.option2 = option2;
        this.vote_desc = vote_desc;
        this.vote_title = vote_title;
        this.counter_op1 = counter_op1;
        this.counter_op2 = counter_op2;
        this.counter_tot = counter_tot;
    }


    public String getCounter_op1() {
        return counter_op1;
    }

    public void setCounter_op1(String counter_op1) {
        this.counter_op1 = counter_op1;
    }

    public String getCounter_op2() {
        return counter_op2;
    }

    public void setCounter_op2(String counter_op2) {
        this.counter_op2 = counter_op2;
    }

    public String getVote_id() {
        return vote_id;
    }

    public void setVote_id(String vote_id) {
        this.vote_id = vote_id;
    }

    public String getCounter_tot() {
        return counter_tot;
    }

    public void setCounter_tot(String counter_tot) {
        this.counter_tot = counter_tot;
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
