package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.services.products.interfaces.*;
import com.jsblanco.springbanking.services.products.interfaces.util.BankProductSubclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BankProductServiceImpl implements BankProductService {

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
            try {
                BankProduct product = (BankProduct) repo.getById(id);
                if (product != null) return product;
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Could not find requested product in our database");
    }

    @Override
    public BankProduct save(BankProduct bankProduct) {
        try {
            get(bankProduct.getId());
        } catch (IllegalArgumentException e) {
            return (BankProduct) fetchProductRepository(bankProduct).save(bankProduct);
        }
        throw new IllegalArgumentException("There is already a product with this ID inside the database");
    }

    @Override
    public BankProduct update(BankProduct bankProduct) {
        BankProduct productInDb = get(bankProduct.getId());
        if (productInDb == null)
            throw new IllegalArgumentException("No such account in the db");
        return (BankProduct) fetchProductRepository(bankProduct).save(bankProduct);
    }

    @Override
    public void delete(Integer id) {
        BankProduct productInDb = get(id);
        if (productInDb == null)
            throw new IllegalArgumentException("No such account in the db");
        fetchProductRepository(productInDb).delete(id);
    }

    @Override
    public Money getProductBalance(Integer id) {
        return get(id).getBalance();
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

        accounts.addAll(creditCardService.getByOwner(owner));
        accounts.addAll(checkingAccountService.getByOwner(owner));
        accounts.addAll(studentCheckingAccountService.getByOwner(owner));
        accounts.addAll(savingsAccountService.getByOwner(owner));

        return accounts.stream().toList();
    }

    @Override
    public List<BankProduct> transferFunds(TransferFundsDao dao, User user) {
        BankProduct emitterAcc = this.get(dao.getEmitterId());
        BankProduct recipientAcc = this.get(dao.getRecipientId());

        if (emitterAcc == null) throw new IllegalArgumentException("Emitter account not found in the db");
        if (recipientAcc == null) throw new IllegalArgumentException("Recipient account not found in the db");

        if (!emitterAcc.getPrimaryOwner().equals(user) && (emitterAcc.getSecondaryOwner() == null || !emitterAcc.getSecondaryOwner().equals(user)))
            throw new IllegalArgumentException("Logged user does not own emitter account");

        if (!recipientAcc.getPrimaryOwner().areNamesEqual(dao.getRecipientName())
                && (recipientAcc.getSecondaryOwner() == null || !recipientAcc.getSecondaryOwner().areNamesEqual(dao.getRecipientName()))) {
            throw new IllegalArgumentException("Specified person does not own recipient account");
        }

        if (emitterAcc.getBalance().getAmount().compareTo(dao.getTransaction().getAmount()) < 0) {
            throw new IllegalArgumentException("Emitter account balance is insufficient to fulfill transactions");
        }

        emitterAcc.decreaseBalance(dao.getTransaction());
        recipientAcc.increaseBalance(dao.getTransaction());

        return new ArrayList<>(Arrays.asList(this.update(emitterAcc), this.update(recipientAcc)));
    }

    private BankProductSubclassService fetchProductRepository(BankProduct bankProduct) {
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
