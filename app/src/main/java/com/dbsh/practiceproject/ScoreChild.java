package com.dbsh.practiceproject;

public class ScoreChild {
    private String subjectName;
    private String subjectRank;
    private String subjectGrade;
    private String subjectDivide;
    private String subjectCredit;
    private String subjectNumber;
    private String subjectProf;

    public ScoreChild(String subjectName, String subjectRank, String subjectGrade, String subjectDivide, String subjectCredit, String subjectNumber, String subjectProf) {
        this.subjectName = subjectName;
        this.subjectRank = subjectRank;
        this.subjectGrade = subjectGrade;
        this.subjectDivide = subjectDivide;
        this.subjectCredit = subjectCredit;
        this.subjectNumber = subjectNumber;
        this.subjectProf = subjectProf;
    }

    public String getSubjectName() { return subjectName; }
    public String getSubjectRank() { return subjectRank; }
    public String getSubjectGrade() { return subjectGrade; }
    public String getSubjectDivide() { return subjectDivide; }
    public String getSubjectCredit() { return subjectCredit; }
    public String getSubjectNumber() { return subjectNumber; }
    public String getSubjectProf() { return subjectProf; }

    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public void setSubjectRank(String subjectRank) { this.subjectRank = subjectRank; }
    public void setSubjectGrade(String subjectGrade) { this.subjectGrade = subjectGrade; }
    public void setSubjectDivide(String subjectDivide) { this.subjectDivide = subjectDivide; }
    public void setSubjectCredit(String subjectCredit) { this.subjectCredit = subjectCredit; }
    public void setSubjectNumber(String subjectNumber) { this.subjectNumber = subjectNumber; }
    public void setSubjectProf(String subjectProf) { this.subjectProf = subjectProf; }
}
