package org.reddrop.reddrop;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by dipto on 11/15/17.
 */

public class UserInfo implements Serializable{
    private String id;
    private String name;
    private String username;
    private String password;
    private String gender;
    private String bloodgroup;

    private String month;
    private String year;
    private String day;

    private String division;
    private String city;
    private String location;

    private String contact;
    private String email;

    private String getNotifications;
    private String isLoggedIn;



    public UserInfo() {

    }

    public UserInfo(String id, String name, String username,
                    String password, String gender, String bloodgroup,
                    String month, String year, String day,
                    String division, String city, String location,
                    String contact, String email, String getNotifications,
                    String isLoggedIn) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.bloodgroup = bloodgroup;
        this.month = month;
        this.year = year;
        this.day = day;
        this.division = division;
        this.city = city;
        this.location = location;
        this.contact = contact;
        this.email = email;
        this.getNotifications = getNotifications;
        this.isLoggedIn = isLoggedIn;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGetNotifications() {
        return getNotifications;
    }

    public void setGetNotifications(String getNotifications) {
        this.getNotifications = getNotifications;
    }

    public String getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(String isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }


}
