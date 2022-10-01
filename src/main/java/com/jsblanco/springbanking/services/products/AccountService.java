package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface AccountService {
    List<Account> getByOwner(AccountHolder owner);
    Account createCheckingAccount(CheckingAccount account);
}
