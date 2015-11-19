package com.example.andrea.musicreview.model;

import java.util.GregorianCalendar;

/**
 * Created by luca-campana on 19/11/15.
 */


public class User {

    public enum Gender {male, female, other}
    private long userID;
    private String name;
    private GregorianCalendar birthday;
    private Gender gender;
    private String mail;

    public User(long userID, String name, GregorianCalendar birthday, Gender gender, String mail) {
        this.userID = userID;
        this.name = name;
        this.birthday = birthday;
        this.gender = gender;
        this.mail = mail;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GregorianCalendar getBirthday() {
        return birthday;
    }

    public void setBirthday(GregorianCalendar birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
