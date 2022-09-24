package com.jsblanco.springbanking.controllers.products;


import com.jsblanco.springbanking.services.products.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;
}
