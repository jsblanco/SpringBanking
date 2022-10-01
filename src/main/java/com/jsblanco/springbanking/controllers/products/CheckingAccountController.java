package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import com.jsblanco.springbanking.services.products.interfaces.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CheckingAccountController {

    @Autowired
    CheckingAccountService checkingAccountService;

    @Autowired
    AccountService accountService;

    @GetMapping("/checking/")
    @ResponseStatus(HttpStatus.OK)
    public List<CheckingAccount> getAllCheckingAccounts() {
        return this.checkingAccountService.getAll();
    }

    @GetMapping("/checking/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CheckingAccount getCheckingAccountById(@PathVariable Integer id) {
        return this.checkingAccountService.getById(id);
    }

    @PostMapping("/checking/")
    @ResponseStatus(HttpStatus.CREATED)
    public Account saveCheckingAccounts(@RequestBody CheckingAccount account) {
        return this.accountService.createCheckingAccount(account);
    }

    @PutMapping("/checking/")
    @ResponseStatus(HttpStatus.CREATED)
    public CheckingAccount updateCheckingAccounts(@RequestBody CheckingAccount account) {
        return this.checkingAccountService.update(account);
    }

    @DeleteMapping("/checking/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCheckingAccounts(@RequestBody CheckingAccount account) {
        this.checkingAccountService.delete(account);
    }
}
