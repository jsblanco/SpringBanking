package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.services.products.interfaces.*;
import com.jsblanco.springbanking.services.products.interfaces.util.BankProductSubclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankProductImpl implements BankProductService {

    @Autowired
    CreditCardService creditCardService;
    @Autowired
    SavingsAccountService savingsAccountService;
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;


    @Override
    public BankProduct get(Integer id) {

        for (BankProductSubclassService<?> repo : new BankProductSubclassService[]{
                creditCardService,
                checkingAccountService,
                savingsAccountService,
                studentCheckingAccountService}) {
            BankProduct product = (BankProduct) repo.getById(id);
            if (product != null) return product;
        }

        throw new IllegalArgumentException("Could not find requested product in our database");
    }

    @Override
    public BankProduct update(BankProduct bankProduct) {
        return (BankProduct) fetchProductRepository(bankProduct).save(bankProduct);
    }

    @Override
    public List<BankProduct> getAll() {
        Set<BankProduct> accounts = new HashSet<>();

        accounts.addAll(creditCardService.getAll());
        accounts.addAll(savingsAccountService.getAll());
        accounts.addAll(checkingAccountService.getAll());
        accounts.addAll(studentCheckingAccountService.getAll());

        return accounts.stream().toList();
    }

    @Override
    public List<BankProduct> getByOwner(AccountHolder owner) {
        Set<BankProduct> accounts = new HashSet<>();

        accounts.addAll(checkingAccountService.getByOwner(owner));
        accounts.addAll(studentCheckingAccountService.getByOwner(owner));
        accounts.addAll(savingsAccountService.getByOwner(owner));

        return accounts.stream().toList();
    }

    @Override
    public List<BankProduct> transferFunds(BankProduct emitter, BankProduct recipient) {
        return null;
    }

    public BankProductSubclassService fetchProductRepository(BankProduct bankProduct) {
        if (CreditCard.class.equals(bankProduct.getClass())) {
            return this.creditCardService;
        }
        if (CheckingAccount.class.equals(bankProduct.getClass())) {
            return this.checkingAccountService;
        }
        if (SavingsAccount.class.equals(bankProduct.getClass())) {
            return this.savingsAccountService;
        }
        if (StudentCheckingAccount.class.equals(bankProduct.getClass())) {
            return this.studentCheckingAccountService;
        }

        throw new IllegalArgumentException("Product not recognised");
    }
}
