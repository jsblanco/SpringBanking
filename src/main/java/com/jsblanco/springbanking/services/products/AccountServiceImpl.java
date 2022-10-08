package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import com.jsblanco.springbanking.services.products.interfaces.CheckingAccountService;
import com.jsblanco.springbanking.services.products.interfaces.SavingsAccountService;
import com.jsblanco.springbanking.services.products.interfaces.StudentCheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
