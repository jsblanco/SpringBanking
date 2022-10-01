package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Override
    public SavingsAccount getById(Integer id) {
        return this.savingsAccountRepository.getSavingsAccountById(id);
    }

    @Override
    public SavingsAccount save(SavingsAccount account) {
        return this.savingsAccountRepository.save(account);
    }

    @Override
    public SavingsAccount update(SavingsAccount account) {
        if (this.savingsAccountRepository.getSavingsAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.savingsAccountRepository.save(account);
    }

    @Override
    public void delete(SavingsAccount account) {
        if (this.savingsAccountRepository.getSavingsAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.savingsAccountRepository.delete(account);
    }

    @Override
    public List<SavingsAccount> getAll() {
        return this.savingsAccountRepository.findAll();
    }

    @Override
    public List<SavingsAccount> getByOwner(AccountHolder owner) {
        Set<SavingsAccount> accounts = new HashSet<>();
        accounts.addAll(this.savingsAccountRepository.getSavingsAccountsByPrimaryOwner(owner));
        accounts.addAll(this.savingsAccountRepository.getSavingsAccountsBySecondaryOwner(owner));
        return accounts.stream().toList();
    }
}
