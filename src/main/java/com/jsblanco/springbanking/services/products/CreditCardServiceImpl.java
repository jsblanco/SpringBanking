package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public CreditCard getById(Integer id) {
        return this.creditCardRepository.getCreditCardById(id);
    }

    @Override
    public CreditCard save(CreditCard account) {
        return this.creditCardRepository.save(account);
    }

    @Override
    public CreditCard update(CreditCard account) {
        if (this.creditCardRepository.getCreditCardById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.creditCardRepository.save(account);
    }

    @Override
    public void delete(CreditCard account) {
        if (this.creditCardRepository.getCreditCardById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.creditCardRepository.delete(account);
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
