package com.jsblanco.springbanking.models.util;

import jakarta.persistence.Embeddable;
import org.springframework.lang.NonNull;

@Embeddable
public class Address {
    @NonNull
    private String door;
    @NonNull
    private String postalCode;
    @NonNull
    private String city;
    @NonNull
    private String country;

    @NonNull
    public String getDoor() {
        return door;
    }

    public void setDoor(@NonNull String houseInfo) {
        this.door = houseInfo;
    }

    @NonNull
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NonNull String postalCode) {
        this.postalCode = postalCode;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull String country) {
        this.country = country;
    }
}
