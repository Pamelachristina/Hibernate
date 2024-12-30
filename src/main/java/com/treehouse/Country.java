package com.treehouse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COUNTRY")
public class Country {

    @Id
    @Column(name = "CODE", length = 3, nullable = false)
    private String code;

    @Column(name = "NAME", length = 32, nullable = false)
    private String name;

    // Remove precision and scale
    @Column(name = "INTERNET_USERS")
    private Double internetUsers;

    // Remove precision and scale
    @Column(name = "ADULT_LITERACY_RATE")
    private Double adultLiteracyRate;

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getInternetUsers() {
        return internetUsers;
    }

    public void setInternetUsers(Double internetUsers) {
        this.internetUsers = internetUsers;
    }

    public Double getAdultLiteracyRate() {
        return adultLiteracyRate;
    }

    public void setAdultLiteracyRate(Double adultLiteracyRate) {
        this.adultLiteracyRate = adultLiteracyRate;
    }

    @Override
    public String toString() {
        return String.format(
                "Country [Code: %s, Name: %s, Internet Users: %.8f, Adult Literacy Rate: %.8f]",
                code, name, internetUsers, adultLiteracyRate
        );
    }
}

