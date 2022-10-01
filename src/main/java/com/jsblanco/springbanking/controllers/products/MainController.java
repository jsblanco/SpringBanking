package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {

    @Autowired
    AccountService accountService;

    public List<Account> getAllAccounts() {
//        return this.accountService.g
        return null;
    }

}
