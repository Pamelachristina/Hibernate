package com.treehouse;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "COUNTRY_DATA")
public class CountryData {
    @Id
    @Column(name = "COUNTRY") // Map to the "COUNTRY" column
    private String country;

    @Column(name = "INTERNET_USERS") // Map to the "INTERNET_USERS" column
    private Double internetUsers;

    @Column(name = "LITERACY_RATE") // Map to the "LITERACY_RATE" column
    private Double literacyRate;

    // Getters and setters
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getLiteracyRate() {
        return literacyRate;
    }

    public void setLiteracyRate(Double literacyRate) {
        this.literacyRate = literacyRate;
    }
}
