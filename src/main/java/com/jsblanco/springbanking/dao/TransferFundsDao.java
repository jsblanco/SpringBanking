package com.jsblanco.springbanking.dao;

import com.jsblanco.springbanking.models.util.Money;

public class TransferFundsDao {
    Money transaction;
    Integer emitterId;
    Integer recipientId;
    String recipientName;

    public TransferFundsDao(Money transaction, Integer emitterId, Integer recipientId, String recipientName) {
        this.transaction = transaction;
        this.emitterId = emitterId;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
    }

    public Money getTransaction() {
        return transaction;
    }

    public void setTransaction(Money transaction) {
        this.transaction = transaction;
    }

    public Integer getEmitterId() {
        return emitterId;
    }

    public void setEmitterId(Integer emitterId) {
        this.emitterId = emitterId;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
