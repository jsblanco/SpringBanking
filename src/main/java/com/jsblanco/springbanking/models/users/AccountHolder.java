package com.jsblanco.springbanking.models.users;

import com.jsblanco.springbanking.models.util.Address;
import com.jsblanco.springbanking.models.products.BankProduct;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.*;

@Entity
public class AccountHolder extends User {
    private Date birthDay;

    @Embedded
    @NonNull
    @AttributeOverrides({
            @AttributeOverride(name = "door", column = @Column(name = "primary_door")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "primary_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "primary_city")),
            @AttributeOverride(name = "country", column = @Column(name = "primary_country"))
    })
    private Address primaryAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "door", column = @Column(name = "mailing_door")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "mailing_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "mailing_city")),
            @AttributeOverride(name = "country", column = @Column(name = "mailing_country"))
    })
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankProduct> primaryOwnedProducts;

    @OneToMany(mappedBy = "secondaryOwner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankProduct> secondaryOwnedProducts;

    public Map<BankProduct, Integer> getProducts() {
        HashMap<BankProduct, Integer> productMap = new HashMap<>();
        primaryOwnedProducts.forEach((product) -> productMap.put(product, product.getId()));
        secondaryOwnedProducts.forEach((product) -> productMap.put(product, product.getId()));

        return productMap;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        if (mailingAddress == null) return primaryAddress;
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<BankProduct> getPrimaryOwnedProducts() {
        return primaryOwnedProducts;
    }

    public void setPrimaryOwnedProducts(List<BankProduct> primaryOwnedProducts) {
        this.primaryOwnedProducts = primaryOwnedProducts;
    }

    public List<BankProduct> getSecondaryOwnedProducts() {
        return secondaryOwnedProducts;
    }

    public void setSecondaryOwnedProducts(List<BankProduct> secondaryOwnedProducts) {
        this.secondaryOwnedProducts = secondaryOwnedProducts;
    }
}
