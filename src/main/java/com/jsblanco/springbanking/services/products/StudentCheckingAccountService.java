package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;

import java.util.List;

public interface StudentCheckingAccountService {
    StudentCheckingAccount getById(Integer id);
    StudentCheckingAccount save(StudentCheckingAccount account);
    StudentCheckingAccount update(StudentCheckingAccount account);
    void delete(StudentCheckingAccount account);

    List<StudentCheckingAccount> getAll();
    List<StudentCheckingAccount> getByOwner(AccountHolder owner);
}
