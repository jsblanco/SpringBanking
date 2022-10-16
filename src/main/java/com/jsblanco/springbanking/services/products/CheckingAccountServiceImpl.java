package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.CheckingAccountService;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class CheckingAccountServiceImpl implements CheckingAccountService {
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    AccountHolderService accountHolderService;

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
    public CheckingAccount createNewProduct(CreateBankProductDao<CheckingAccount> dao) {
        CheckingAccount checkingAccount = dao.getProduct();
        checkingAccount.setPrimaryOwner(this.accountHolderService.getById(dao.getPrimaryOwnerId()));
        try {
            if (dao.getSecondaryOwnerId() != null)
                checkingAccount.setSecondaryOwner(this.accountHolderService.getById(dao.getSecondaryOwnerId()));
        } catch (ResponseStatusException ignored) {}

        return this.save(checkingAccount);
    }

    @Override
    public List<CheckingAccount> getByOwner(AccountHolder owner) {
        Set<CheckingAccount> accounts = new HashSet<>();
        accounts.addAll(this.checkingAccountRepository.getCheckingAccountsByPrimaryOwner(owner));
        accounts.addAll(this.checkingAccountRepository.getCheckingAccountsBySecondaryOwner(owner));
        return accounts.stream().toList();
    }
}
