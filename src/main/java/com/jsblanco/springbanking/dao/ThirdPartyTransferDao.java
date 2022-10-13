package com.jsblanco.springbanking.dao;

import com.jsblanco.springbanking.models.util.Money;

public class ThirdPartyTransferDao {
    Money transfer;
    Integer accountId;
    String secretKey;

    public Money getTransfer() {
        return transfer;
    }

    public void setTransfer(Money transfer) {
        this.transfer = transfer;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
