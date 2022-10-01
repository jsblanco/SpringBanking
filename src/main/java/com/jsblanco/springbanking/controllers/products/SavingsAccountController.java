package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.services.products.interfaces.SavingsAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SavingsAccountController {

    @Autowired
    SavingsAccountService savingsAccountService;

    @GetMapping("/savings/")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAllSavingsAccounts() {
        return this.savingsAccountService.getAll();
    }

    @GetMapping("/savings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SavingsAccount getSavingsAccountById(@PathVariable Integer id) {
        return this.savingsAccountService.getById(id);
    }

    @PostMapping("/savings/")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount saveSavingsAccounts(@RequestBody SavingsAccount account) {
        return this.savingsAccountService.save(account);
    }

    @PutMapping("/savings/")
    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount updateSavingsAccounts(@RequestBody SavingsAccount account) {
        return this.savingsAccountService.update(account);
    }

    @DeleteMapping("/savings/")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSavingsAccounts(@RequestBody SavingsAccount account) {
        this.savingsAccountService.delete(account);
    }
}
