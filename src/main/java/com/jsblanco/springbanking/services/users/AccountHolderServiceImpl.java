package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Override
    public AccountHolder getById(Integer id) {
        Optional<AccountHolder> accountHolder = this.accountHolderRepository.findById(id);
        if (accountHolder.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return accountHolder.get();
    }

    @Override
    public AccountHolder save(AccountHolder account) {
        return this.accountHolderRepository.save(account);
    }

    @Override
    public AccountHolder update(AccountHolder account) {
        Optional<AccountHolder> accountHolder = this.accountHolderRepository.findById(account.getId());
        if (accountHolder.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        if (account.getPassword() == null)
            account.setPassword(accountHolder.get().getPassword());
        return this.accountHolderRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<AccountHolder> accountHolder = this.accountHolderRepository.findById(id);
        if (accountHolder.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.accountHolderRepository.delete(accountHolder.get());
    }

    @Override
    public List<AccountHolder> getAll() {
        return this.accountHolderRepository.findAll();
    }

    @Override
    public AccountHolder getByUsername(String name) {
        return this.accountHolderRepository.getAccountHolderByUsername(name);
    }
}
