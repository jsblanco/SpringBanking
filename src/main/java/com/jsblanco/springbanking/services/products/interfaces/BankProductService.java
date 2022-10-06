package com.jsblanco.springbanking.services.products.interfaces;

import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Money;

import java.util.List;

public interface BankProductService {
    BankProduct get(Integer id);
    BankProduct update(BankProduct bankProduct);

    List<BankProduct> getAll();
    List<BankProduct> getByOwner(AccountHolder owner);
    List<BankProduct> transferFunds(Money balance, BankProduct emitter, BankProduct recipient);
}
