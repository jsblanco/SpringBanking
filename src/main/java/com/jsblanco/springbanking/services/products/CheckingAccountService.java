package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface CheckingAccountService {

    CheckingAccount getById(Integer id);
    CheckingAccount save(CheckingAccount account);
    CheckingAccount update(CheckingAccount account);
    void delete(CheckingAccount account);
    List<CheckingAccount> getAll();
    List<CheckingAccount> getByOwner(AccountHolder owner);
}
