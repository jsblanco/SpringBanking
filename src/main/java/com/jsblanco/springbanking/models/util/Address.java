package com.jsblanco.springbanking.models.util;

import javax.persistence.Embeddable;
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

    public Address() {
    }

    public Address(@NonNull String door, @NonNull String postalCode, @NonNull String city, @NonNull String country) {
        this.door = door;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

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

    @Override
    public String toString() {
        return "Address{" +
                "door='" + door + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Address address)
            return postalCode.equals(address.getPostalCode())
                    && city.equals(address.getCity())
                    && country.equals(address.getCountry())
                    && door.equals(address.getDoor());
        return false;
    }
}
