package com.dbsh.practiceproject;

import android.app.Application;

import java.util.ArrayList;

public class userClass extends Application {
    private String id;              // 학번
    private String korName;         // 이름
    private String phoneNumber;     // 전화번호
    private String colName;         // 단대
    private String deptName;        // 학과
    private String emailAddress;    // 이메일주소
    private String webmailAddress;  // 학교메일주소
    private String tutorName;       // 멘토교수
    private String schYear;         // 년도
    private String schTerm;         // 학기
    private String schYR;           // 학년
    private String sch_REG_STAT_NM; // 재학 휴학 여부(한글표기)
    private String token;           // 로그인 토큰
    private ArrayList<String> lectlist;         // 해당학기 수강과목 학수번호
    private ArrayList<String> lectnumlist;      // 해당학기 수강과목 학수번호
    private ArrayList<String> lecttimelist;     // 해당학기 수강과목 수업시간
    private ArrayList<String> lectproflist;     // 해당학기 수강과목 담당교수
    private ArrayList<String> yearlist;    // 년도 정리

    public userClass() {
        lectlist = new ArrayList<>();
        lectnumlist = new ArrayList<>();
        lecttimelist = new ArrayList<>();
        lectproflist = new ArrayList<>();
        yearlist = new ArrayList<>();
    }

    public void setId(String id) { this.id = id; }
    public void setKorName(String korName) { this.korName = korName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setMajor(String colName, String deptName) {
        this.colName = colName;
        this.deptName = deptName;
    }
    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }
    public void setWebmailAddress(String webmailAddress) { this.webmailAddress = webmailAddress; }
    public void setTutorName(String tutorName) { this.tutorName = tutorName; }
    public void setSchInfo(String year, String term, String schYR, String sch_REG_STAT_NM) {
        this.schYear = year;
        this.schTerm = term;
        this.schYR = schYR;
        this.sch_REG_STAT_NM = sch_REG_STAT_NM;
    }
    public void setToken(String token) { this.token = token; }
    public void addLectlist(String lect) {this.lectlist.add(lect); }
    public void addLectnumlist(String lect_num) {this.lectnumlist.add(lect_num); }
    public void addLecttimelist(String lect_time) {this.lecttimelist.add(lect_time); }
    public void addLectproflist(String prof_num) {this.lectproflist.add(prof_num); }
    public void addYearlist(String year) { this.yearlist.add(year); }

    public String getId() { return this.id; }
    public String getKorName() { return this.korName; }
    public String getPhoneNumber() { return this.phoneNumber; }
    public String getColName() { return this.colName; }
    public String getDeptName() { return this.deptName; }
    public String getEmailAddress() { return this.emailAddress; }
    public String getWebmailAddress() { return this.webmailAddress; }
    public String getTutorName() { return this.tutorName; }
    public String getSchYear() { return this.schYear; }
    public String getSchTerm() { return this.schTerm; }
    public String getSchYR() { return this.schYR; }
    public String getToken() { return this.token; }
    public ArrayList<String> getLectlist() { return this.lectlist; }
    public ArrayList<String> getLectnumlist() { return this.lectnumlist; }
    public ArrayList<String> getLecttimelist() { return this.lecttimelist; }
    public ArrayList<String> getLectproflist() { return this.lectproflist; }
    public ArrayList<String> getYearlist() { return this.yearlist; }

    public void clearLectlist() { this.lectlist.clear(); }
    public void clearLectnumlist() { this.lectnumlist.clear(); }
    public void clearLecttimelist() { this.lecttimelist.clear(); }
    public void clearLectproflist() { this.lectproflist.clear(); }
    public void clearYearlist() {
        this.yearlist.clear();
    }
}
