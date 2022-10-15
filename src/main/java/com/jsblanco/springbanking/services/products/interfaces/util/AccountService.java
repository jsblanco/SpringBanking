package com.jsblanco.springbanking.services.products.interfaces.util;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface AccountService {
    Account createCheckingAccount(CheckingAccount account);
    List<Account> getAll();
    List<Account> getByOwner(AccountHolder owner);
}
