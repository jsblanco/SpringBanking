package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.services.products.CheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckingAccountController {

    @Autowired
    CheckingAccountService checkingAccountService;

    @GetMapping("/")
    public void prueba(){

    }
}
