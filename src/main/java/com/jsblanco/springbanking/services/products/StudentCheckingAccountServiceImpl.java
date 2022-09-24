package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentCheckingAccountServiceImpl implements StudentCheckingAccountService {

    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;
}
