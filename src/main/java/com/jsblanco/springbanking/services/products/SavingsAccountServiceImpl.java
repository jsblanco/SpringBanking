package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.repositories.products.SavingsAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

    @Autowired
    SavingsAccountRepository savingsAccountRepository;
}
