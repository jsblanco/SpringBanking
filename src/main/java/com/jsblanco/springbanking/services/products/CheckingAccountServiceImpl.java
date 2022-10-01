package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Override
    public CheckingAccount getById(Integer id) {
        return this.checkingAccountRepository.getCheckingAccountById(id);
    }

    @Override
    public CheckingAccount save(CheckingAccount account) {
        return this.checkingAccountRepository.save(account);
    }

    @Override
    public CheckingAccount update(CheckingAccount account) {
        if (this.checkingAccountRepository.getCheckingAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.checkingAccountRepository.save(account);
    }

    @Override
    public void delete(CheckingAccount account) {
        if (this.checkingAccountRepository.getCheckingAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.checkingAccountRepository.delete(account);
    }

    @Override
    public List<CheckingAccount> getAll() {
        return this.checkingAccountRepository.findAll();
    }

    @Override
    public List<CheckingAccount> getByOwner(AccountHolder owner) {
        Set<CheckingAccount> accounts = new HashSet<>();
        accounts.addAll(this.checkingAccountRepository.getCheckingAccountsByPrimaryOwner(owner));
        accounts.addAll(this.checkingAccountRepository.getCheckingAccountsBySecondaryOwner(owner));
        return accounts.stream().toList();
    }
}
