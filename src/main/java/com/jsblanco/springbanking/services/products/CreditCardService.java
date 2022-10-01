package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface CreditCardService {

    CreditCard getById(Integer id);
    CreditCard save(CreditCard account);
    CreditCard update(CreditCard account);
    void delete(CreditCard account);
    List<CreditCard> getAll();
    List<CreditCard> getByOwner(AccountHolder owner);
}
