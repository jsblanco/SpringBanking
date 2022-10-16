package com.jsblanco.springbanking.services.products.interfaces;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;

import java.util.List;

public interface BankProductService {
    BankProduct get(Integer id);
    BankProduct save(CreateBankProductDao<BankProduct> dao);
    BankProduct update(BankProduct bankProduct);
    void delete(Integer id);

    BankProduct getProductData(Integer id, User user);
    Money getProductBalance(Integer id, User user);
    Money modifyProductBalance(Integer id, Money balanceChange);

    List<BankProduct> getAll();
    List<BankProduct> getByOwner(AccountHolder owner);
}
