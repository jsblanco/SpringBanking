package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.services.products.interfaces.StudentCheckingAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class StudentCheckingAccountController {

    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;


    @GetMapping("/student/")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentCheckingAccount> getAllStudentCheckingAccounts() {
        return this.studentCheckingAccountService.getAll();
    }

    @GetMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentCheckingAccount getStudentCheckingAccountById(@PathVariable Integer id) {
        return this.studentCheckingAccountService.getById(id);
    }

    @PostMapping("/student/")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentCheckingAccount saveStudentCheckingAccounts(@RequestBody @Valid CreateBankProductDao<StudentCheckingAccount> account) {
        return this.studentCheckingAccountService.createNewProduct(account);
    }

    @PutMapping("/student/")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentCheckingAccount updateStudentCheckingAccounts(@RequestBody @Valid StudentCheckingAccount account) {
        return this.studentCheckingAccountService.update(account);
    }

    @DeleteMapping("/student/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudentCheckingAccounts(@PathVariable Integer id) {
        this.studentCheckingAccountService.delete(id);
    }
}
