package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.repositories.products.StudentCheckingAccountRepository;
import com.jsblanco.springbanking.services.products.interfaces.StudentCheckingAccountService;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentCheckingAccountServiceImpl implements StudentCheckingAccountService {
    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    AccountHolderService accountHolderService;

    @Override
    public StudentCheckingAccount getById(Integer id) {
        Optional<StudentCheckingAccount> studentCheckingAccount = this.studentCheckingAccountRepository.findById(id);
        if (studentCheckingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return studentCheckingAccount.get();
    }

    @Override
    public StudentCheckingAccount createNewProduct(CreateBankProductDao<StudentCheckingAccount> dao) {
        return this.save(this.populateBankProduct(dao, this.accountHolderService));
    }

    @Override
    public StudentCheckingAccount save(StudentCheckingAccount account) {
        return this.studentCheckingAccountRepository.save(account);
    }

    @Override
    public StudentCheckingAccount update(StudentCheckingAccount account) {
        Optional<StudentCheckingAccount> studentCheckingAccount = this.studentCheckingAccountRepository.findById(account.getId());
        if (studentCheckingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return this.studentCheckingAccountRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<StudentCheckingAccount> studentCheckingAccount = this.studentCheckingAccountRepository.findById(id);
        if (studentCheckingAccount.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.studentCheckingAccountRepository.delete(studentCheckingAccount.get());
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
