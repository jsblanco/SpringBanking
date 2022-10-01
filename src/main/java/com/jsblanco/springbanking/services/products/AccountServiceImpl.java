package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.DateUtils;
import com.jsblanco.springbanking.repositories.products.CheckingAccountRepository;
import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
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
    public List<Account> getByOwner(AccountHolder owner) {
        Set<Account> accounts = new HashSet<>();

        accounts.addAll(checkingAccountService.getByOwner(owner));
        accounts.addAll(studentCheckingAccountService.getByOwner(owner));
        accounts.addAll(savingsAccountService.getByOwner(owner));

        return accounts.stream().toList();
    }

    @Override
    public Account createCheckingAccount(CheckingAccount account) {
        LocalDate birthday = DateUtils.getDateLocalValue(account.getPrimaryOwner().getBirthDay());
        if (Period.between(birthday, LocalDate.now()).getYears() < 24)
            return this.studentCheckingAccountService.save(new StudentCheckingAccount(account));
        return this.checkingAccountService.save(account);
    }


}
