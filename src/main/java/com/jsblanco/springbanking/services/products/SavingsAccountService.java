package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface SavingsAccountService {
    SavingsAccount getById(Integer id);

    SavingsAccount save(SavingsAccount account);
    SavingsAccount update(SavingsAccount account);
    void delete(SavingsAccount account);

    List<SavingsAccount> getAll();
    List<SavingsAccount> getByOwner(AccountHolder owner);
}
