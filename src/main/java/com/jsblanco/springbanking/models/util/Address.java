package com.jsblanco.springbanking.models.util;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Address {
    @NotNull
    private String door;
    @NotNull
    private String postalCode;
    @NotNull
    private String city;
    @NotNull
    private String country;

    public Address() {
    }

    public Address(@NotNull String door, @NotNull String postalCode, @NotNull String city, @NotNull String country) {
        this.door = door;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    @NotNull
    public String getDoor() {
        return door;
    }

    public void setDoor(@NotNull String houseInfo) {
        this.door = houseInfo;
    }

    @NotNull
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NotNull String postalCode) {
        this.postalCode = postalCode;
    }

    @NotNull
    public String getCity() {
        return city;
    }

    public void setCity(@NotNull String city) {
        this.city = city;
    }

    @NotNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NotNull String country) {
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
