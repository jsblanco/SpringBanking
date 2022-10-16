package com.jsblanco.springbanking.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jsblanco.springbanking.models.products.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

public class CreateBankProductDao<T> {
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value= CreditCard.class , name="creditCard"),
            @JsonSubTypes.Type(value= SavingsAccount.class, name="savingsAccount"),
            @JsonSubTypes.Type(value= CheckingAccount.class, name="checkingAccount"),
            @JsonSubTypes.Type(value= StudentCheckingAccount.class, name="studentCheckingAccount")
    })
    T product;
    Integer primaryOwnerId;
    @JsonInclude(NON_NULL)
    Integer secondaryOwnerId;

    public CreateBankProductDao() {
    }

    public CreateBankProductDao(T product, Integer primaryOwnerId) {
        this.product = product;
        this.primaryOwnerId = primaryOwnerId;
    }

    public CreateBankProductDao(T product, Integer primaryOwnerId, Integer secondaryOwnerId) {
        this.product = product;
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public T getProduct() {
        return product;
    }

    public void setProduct(T product) {
        this.product = product;
    }

    public Integer getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Integer primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Integer getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Integer secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }
}
