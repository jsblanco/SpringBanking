package com.jsblanco.springbanking.model.users;

import com.jsblanco.springbanking.model.Address;
import com.jsblanco.springbanking.model.accounts.BankProduct;
import jakarta.persistence.*;

import java.lang.reflect.Array;
import java.util.*;

@Entity
public class AccountHolder extends User {
    private Date birthDay;

    @OneToOne
    @JoinColumn(name = "primary_address", nullable = false)
    private Address primaryAddress;

    @OneToOne
    @JoinColumn(name = "mailing_address", nullable = true)
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
}
