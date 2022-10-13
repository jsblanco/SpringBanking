package com.jsblanco.springbanking.services.products.interfaces;

import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;

import java.util.List;

public interface BankProductService {
    BankProduct get(Integer id);
    BankProduct save(BankProduct bankProduct);
    BankProduct update(BankProduct bankProduct);
    void delete(Integer id);

    Money getProductBalance(Integer id, User user);
    Money modifyProductBalance(Integer id, Money balanceChange, User user);

    List<BankProduct> getAll();
    List<BankProduct> getByOwner(AccountHolder owner);
    List<BankProduct> transferFunds(TransferFundsDao dao, User user);
}
