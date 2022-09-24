package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.services.products.StudentCheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentCheckingAccountController {

    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;
}
