package com.dbsh.practiceproject;

import java.util.ArrayList;

public class ScoreParent {
    private String yearterm;
    private String credit;
    private String grade;
    private String rank;

    public ScoreParent(String yearterm, String credit, String grade, String rank) {
        this.yearterm = yearterm;
        this.credit = credit;
        this.grade = grade;
        this.rank = rank;
    }

    public String getYearterm() { return yearterm; }
    public String getCredit() { return credit; }
    public String getGrade() { return grade; }
    public String getRank() { return rank; }

    public void setYearterm(String yearterm) { this.yearterm = yearterm; }
    public void setCredit(String credit) { this.credit = credit; }
    public void setGrade(String grade) { this.grade = grade; }
    public void setRank(String rank) { this.rank = rank; }
}
