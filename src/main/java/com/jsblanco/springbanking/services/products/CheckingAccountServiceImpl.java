package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Override
    public CheckingAccount getById(Integer id) {
        Optional<CheckingAccount> checkingAccount = this.checkingAccountRepository.findById(id);
        if (checkingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return checkingAccount.get();
    }

    @Override
    public CheckingAccount save(CheckingAccount account) {
        return this.checkingAccountRepository.save(account);
    }

    @Override
    public CheckingAccount update(CheckingAccount account) {
        Optional<CheckingAccount> checkingAccount = this.checkingAccountRepository.findById(account.getId());
        if (checkingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return this.checkingAccountRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<CheckingAccount> checkingAccount = this.checkingAccountRepository.findById(id);
        if (checkingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.checkingAccountRepository.delete(checkingAccount.get());
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
