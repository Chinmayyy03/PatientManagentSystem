package com.example.patientmanagementapp;

import java.util.Date;
import java.util.List;

public class Patientinfo {

    private String  p_age;


    private String imagePath;

    private String date;
    private String patient_id;
    private String p_Name;
    private String p_gender;

    private String p_Disease;
    private String p_Diseasedescription;

    public Patientinfo(){

    }
    public Patientinfo(String patient_id, String date, String p_Name, String p_age,  String p_Disease, String p_Diseasedescription) {
        this.patient_id = patient_id;
        this.p_age = p_age;
        this.date = date;
        this.p_Name = p_Name;
//        this.p_gender = p_gender;
        this.p_Disease = p_Disease;
        this.p_Diseasedescription = p_Diseasedescription;
    }

    public Patientinfo(String patient_id, String date, String p_Name, String p_age, String p_gender, String p_Disease, String p_Diseasedescription) {
        this.p_age = p_age;
//        this.imageUrl = imageUrl;
        this.date = date;
        this.patient_id = patient_id;
        this.p_Name = p_Name;
        this.p_gender = p_gender;
        this.p_Disease = p_Disease;
        this.p_Diseasedescription = p_Diseasedescription;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getP_age() {
        return p_age;
    }

    public void setP_age(String p_age) {
        this.p_age = p_age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getP_Name() {
        return p_Name;
    }

    public void setP_Name(String p_Name) {
        this.p_Name = p_Name;
    }

    public String getP_gender() {
        return p_gender;
    }

    public void setP_gender(String p_gender) {
        this.p_gender = p_gender;
    }

    public String getP_Disease() {
        return p_Disease;
    }

    public void setP_Disease(String p_Disease) {
        this.p_Disease = p_Disease;
    }

    public String getP_Diseasedescription() {
        return p_Diseasedescription;
    }

    public void setP_Diseasedescription(String p_Diseasedescription) {
        this.p_Diseasedescription = p_Diseasedescription;
    }
}
