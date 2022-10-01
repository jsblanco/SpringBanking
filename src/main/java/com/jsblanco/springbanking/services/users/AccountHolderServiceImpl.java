package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.users.AccountHolderRepository;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountHolderServiceImpl implements AccountHolderService {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Override
    public AccountHolder getById(Integer id) {
        return this.accountHolderRepository.getAccountHolderById(id);
    }

    @Override
    public AccountHolder save(AccountHolder account) {
        return this.accountHolderRepository.save(account);
    }

    @Override
    public AccountHolder update(AccountHolder account) {
        if (this.accountHolderRepository.getAccountHolderById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.accountHolderRepository.save(account);
    }

    @Override
    public void delete(AccountHolder account) {
        if (this.accountHolderRepository.getAccountHolderById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.accountHolderRepository.delete(account);
    }

    @Override
    public List<AccountHolder> getAll() {
        return this.accountHolderRepository.findAll();
    }
}
