package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.StudentCheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class StudentCheckingAccountServiceImpl implements StudentCheckingAccountService {

    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;

    @Override
    public StudentCheckingAccount getById(Integer id) {
        return this.studentCheckingAccountRepository.getStudentCheckingAccountById(id);
    }

    @Override
    public StudentCheckingAccount save(StudentCheckingAccount account) {
        return this.studentCheckingAccountRepository.save(account);
    }

    @Override
    public StudentCheckingAccount update(StudentCheckingAccount account) {
        if (this.studentCheckingAccountRepository.getStudentCheckingAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.studentCheckingAccountRepository.save(account);
    }

    @Override
    public void delete(StudentCheckingAccount account) {
        if (this.studentCheckingAccountRepository.getStudentCheckingAccountById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.studentCheckingAccountRepository.delete(account);
    }

    @Override
    public List<StudentCheckingAccount> getAll() {
        return this.studentCheckingAccountRepository.findAll();
    }

    @Override
    public List<StudentCheckingAccount> getByOwner(AccountHolder owner) {
        Set<StudentCheckingAccount> accounts = new HashSet<>();
        accounts.addAll(this.studentCheckingAccountRepository.getStudentCheckingAccountsByPrimaryOwner(owner));
        accounts.addAll(this.studentCheckingAccountRepository.getStudentCheckingAccountsBySecondaryOwner(owner));
        return accounts.stream().toList();
    }
}
