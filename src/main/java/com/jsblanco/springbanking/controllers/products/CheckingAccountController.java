package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.services.products.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CheckingAccountController {

    @Autowired
    CheckingAccountService checkingAccountService;

    @GetMapping("/checking/")
    public List<CheckingAccount> getAllCheckingAccounts() {
        return this.checkingAccountService.getAll();
    }
}
