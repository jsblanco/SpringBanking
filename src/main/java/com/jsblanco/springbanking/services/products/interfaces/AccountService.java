package com.jsblanco.springbanking.services.products.interfaces;

import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.User;

import java.util.List;

public interface AccountService {
    Account get(Integer id);
    Account save(Account bankProduct);
    Account update(Account bankProduct);

    Account createCheckingAccount(CheckingAccount account);
    List<Account> getAll();
    List<Account> getByOwner(AccountHolder owner);

    List<Account> transferFunds(TransferFundsDao dao, User user);
    void thirdPartyOperation(String hashedKey, ThirdPartyTransferDao transferData, User user);
}
