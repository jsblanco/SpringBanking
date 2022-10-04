package com.jsblanco.springbanking.controllers.users;


import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AccountHolderController {

    @Autowired
    AccountHolderService accountHolderService;

    @GetMapping("/holder/")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolder> getAllAccountHolders() {
        return this.accountHolderService.getAll();
    }

    @GetMapping("/holder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountHolder getAccountHolderById(@PathVariable Integer id) {
        return this.accountHolderService.getById(id);
    }

    @PostMapping("/holder/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder saveAccountHolder(@RequestBody AccountHolder accountHolder) {
        return this.accountHolderService.save(accountHolder);
    }

    @PutMapping("/holder/")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder updateAccountHolder(@RequestBody AccountHolder accountHolder) {
        return this.accountHolderService.update(accountHolder);
    }

    @DeleteMapping("/holder/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAccountHolder(@PathVariable Integer id) {
        this.accountHolderService.delete(id);
    }
}
