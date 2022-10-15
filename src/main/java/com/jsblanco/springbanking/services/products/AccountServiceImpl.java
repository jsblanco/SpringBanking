package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Status;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import com.jsblanco.springbanking.services.products.interfaces.CheckingAccountService;
import com.jsblanco.springbanking.services.products.interfaces.SavingsAccountService;
import com.jsblanco.springbanking.services.products.interfaces.StudentCheckingAccountService;
import com.jsblanco.springbanking.services.products.interfaces.util.BankProductSubclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    SavingsAccountService savingsAccountService;
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;

    public Account get(Integer id) {

        for (BankProductSubclassService<?> repo : new BankProductSubclassService[]{
                checkingAccountService,
                savingsAccountService,
                studentCheckingAccountService}) {
            try {
                Account product = (Account) repo.getById(id);
                if (product != null) return product;
            } catch (Exception ignored) {
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such product exists in the database");
    }

    public Account save(Account bankProduct) {
        try {
            get(bankProduct.getId());
        } catch (ResponseStatusException e) {
            return (Account) fetchProductRepository(bankProduct).save(bankProduct);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a product with this ID inside the database");
    }

    public Account update(Account bankProduct) {
        try {
            get(bankProduct.getId());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such product exists in the database");
        }
        return (Account) fetchProductRepository(bankProduct).save(bankProduct);
    }

    @Override
    public Account createCheckingAccount(CheckingAccount account) {
        LocalDate birthday = account.getPrimaryOwner().getBirthDay();
        if (Period.between(birthday, LocalDate.now()).getYears() < 24)
            return this.studentCheckingAccountService.save(new StudentCheckingAccount(account));
        return this.checkingAccountService.save(account);
    }

    @Override
    public List<Account> getAll() {
        Set<Account> accounts = new HashSet<>();

        accounts.addAll(checkingAccountService.getAll());
        accounts.addAll(studentCheckingAccountService.getAll());
        accounts.addAll(savingsAccountService.getAll());

        return accounts.stream().toList();
    }

    @Override
    public List<Account> getByOwner(AccountHolder owner) {
        Set<Account> accounts = new HashSet<>();

        accounts.addAll(checkingAccountService.getByOwner(owner));
        accounts.addAll(studentCheckingAccountService.getByOwner(owner));
        accounts.addAll(savingsAccountService.getByOwner(owner));

        return accounts.stream().toList();
    }

    @Override
    public List<Account> transferFunds(TransferFundsDao dao, User user) {
        Account emitterAcc = this.get(dao.getEmitterId());
        Account recipientAcc = this.get(dao.getRecipientId());

        if (emitterAcc == null) throw new IllegalArgumentException("Emitter account not found in the db");
        if (recipientAcc == null) throw new IllegalArgumentException("Recipient account not found in the db");

        if (emitterAcc.getStatus().equals(Status.FROZEN) || recipientAcc.getStatus().equals(Status.FROZEN))
            throw new IllegalArgumentException("Cannot transfer funds involving frozen accounts");

        if (!emitterAcc.getPrimaryOwner().equals(user) && (emitterAcc.getSecondaryOwner() == null || !emitterAcc.getSecondaryOwner().equals(user)))
            throw new IllegalArgumentException("Logged user does not own emitter account");

        if (recipientAcc.getPrimaryOwner().areNamesDifferent(dao.getRecipientName())
                && (recipientAcc.getSecondaryOwner() == null || recipientAcc.getSecondaryOwner().areNamesDifferent(dao.getRecipientName()))) {
            throw new IllegalArgumentException("Specified person does not own recipient account");
        }

        if (emitterAcc.getBalance().getAmount().compareTo(dao.getTransaction().getAmount()) < 0) {
            throw new IllegalArgumentException("Emitter account balance is insufficient to fulfill transactions");
        }

        emitterAcc.decreaseBalance(dao.getTransaction());
        recipientAcc.increaseBalance(dao.getTransaction());

        return new ArrayList<>(Arrays.asList(this.update(emitterAcc), this.update(recipientAcc)));
    }

    public void thirdPartyOperation(String hashedKey, ThirdPartyTransferDao transferData, User user) {
        if (!(user instanceof ThirdParty))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This action can only be performed by a third party user");
        if (!((ThirdParty) user).getHashedKey().equals(hashedKey))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Provided hashed key does not match logged user");

        Account account = get(transferData.getAccountId());
        if (!account.getSecretKey().equals(transferData.getSecretKey()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Provided secret key does not match requested account");

        if (transferData.getTransfer().getAmount().compareTo(BigDecimal.ZERO) > 0)
            account.increaseBalance(transferData.getTransfer());
        else
            account.decreaseBalance(transferData.getTransfer());

        this.update(account);
    }

    private BankProductSubclassService fetchProductRepository(Account bankProduct) {
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
