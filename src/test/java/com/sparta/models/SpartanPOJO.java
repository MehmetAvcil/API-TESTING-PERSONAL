package com.sparta.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SpartanPOJO {
    private String firstName;
    private String lastName;
    private CoursePOJO course;

    public SpartanPOJO() {}

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

    public CoursePOJO getCourse() {
        return course;
    }

    public void setCourse(CoursePOJO course) {
        this.course = course;
    }
}