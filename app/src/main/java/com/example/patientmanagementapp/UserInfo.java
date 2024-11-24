package com.example.patientmanagementapp;

import android.net.Uri;
import android.util.Log;

public class UserInfo {
    // Declare all fields
    public String userId, firstName, lastName, phoneNumber, email, address;
    public String gender = "", city = "", state = "", nationality = "";
    String dob = "";
    String imgUrl = ""; // Default empty string for imgUrl

    // Default constructor for Firebase and general use
    public UserInfo() {
        // Firebase requires an empty constructor for deserialization
    }

    // Constructor that accepts only the required fields (email, firstName, etc.)
    public UserInfo(String email, String firstName, String lastName, String phoneNumber, String userId) {
        Log.d("UserInfo", "UserInfo: " + userId + " " + firstName + " " + lastName + " " + phoneNumber + " " + email);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userId = userId;


//        this.imgUrl = "";
    }

    // Constructor with all fields (including imgUrl)
    public UserInfo(String userId, String firstName, String phoneNumber, String email,
                    String gender, String city, String state, String nationality,
                    String address, String dob, String imgUrl) {
        this.userId = userId;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.gender = gender;
        this.city = city;
        this.state = state;
        this.nationality = nationality;
        this.address = address;
        this.dob = dob;


        this.imgUrl =  imgUrl;
    }

    // Getter and Setter for imgUrl
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // Getter and Setter for other fields (dob, city, etc.)
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
