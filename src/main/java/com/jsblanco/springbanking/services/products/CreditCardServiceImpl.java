package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import com.jsblanco.springbanking.services.products.interfaces.CreditCardService;
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
public class CreditCardServiceImpl implements CreditCardService {
    @Autowired
    CreditCardRepository creditCardRepository;
    @Autowired
    AccountHolderService accountHolderService;

    @Override
    public CreditCard getById(Integer id) {
        Optional<CreditCard> creditCard = this.creditCardRepository.findById(id);
        if (creditCard.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return creditCard.get();
    }

    @Override
    public CreditCard createNewProduct(CreateBankProductDao<CreditCard> dao) {
        return this.save(this.populateBankProduct(dao, this.accountHolderService));
    }

    @Override
    public CreditCard save(CreditCard account) {
        return this.creditCardRepository.save(account);
    }

    @Override
    public CreditCard update(CreditCard account) {
        Optional<CreditCard> creditCard = this.creditCardRepository.findById(account.getId());
        if (creditCard.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return this.creditCardRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<CreditCard> creditCard = this.creditCardRepository.findById(id);
        if (creditCard.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.creditCardRepository.delete(creditCard.get());
    }

    @Override
    public List<CreditCard> getAll() {
        return this.creditCardRepository.findAll();
    }

    @Override
    public List<CreditCard> getByOwner(AccountHolder owner) {
        Set<CreditCard> accounts = new HashSet<>();
        accounts.addAll(this.creditCardRepository.getCreditCardsByPrimaryOwner(owner));
        accounts.addAll(this.creditCardRepository.getCreditCardsBySecondaryOwner(owner));
        return accounts.stream().toList();
    }
}
