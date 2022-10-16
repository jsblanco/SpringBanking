package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.SavingsAccountService;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {
    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    AccountHolderService accountHolderService;

    @Override
    public SavingsAccount getById(Integer id) {
        Optional<SavingsAccount> savingsAccount = this.savingsAccountRepository.findById(id);
        if (savingsAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return savingsAccount.get();
    }

    @Override
    public SavingsAccount createNewProduct(CreateBankProductDao<SavingsAccount> dao) {
        SavingsAccount savingsAccount = dao.getProduct();
        savingsAccount.setPrimaryOwner(this.accountHolderService.getById(dao.getPrimaryOwnerId()));
        try {
            if (dao.getSecondaryOwnerId() != null)
                savingsAccount.setSecondaryOwner(this.accountHolderService.getById(dao.getSecondaryOwnerId()));
        } catch (ResponseStatusException ignored) {}

        return this.save(savingsAccount);
    }

    @Override
    public SavingsAccount save(SavingsAccount account) {
        return this.savingsAccountRepository.save(account);
    }

    @Override
    public SavingsAccount update(SavingsAccount account) {
        Optional<SavingsAccount> savingsAccount = this.savingsAccountRepository.findById(account.getId());
        if (savingsAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return this.savingsAccountRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<SavingsAccount> savingsAccount = this.savingsAccountRepository.findById(id);
        if (savingsAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.savingsAccountRepository.delete(savingsAccount.get());
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
