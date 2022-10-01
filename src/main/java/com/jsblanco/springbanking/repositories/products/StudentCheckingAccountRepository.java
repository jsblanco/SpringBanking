package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.StudentCheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentCheckingAccountRepository extends JpaRepository<StudentCheckingAccount, Integer> {
    StudentCheckingAccount getStudentCheckingAccountById(Integer id);
    List<StudentCheckingAccount> getStudentCheckingAccountsByPrimaryOwner(AccountHolder primaryOwner);
    List<StudentCheckingAccount> getStudentCheckingAccountsBySecondaryOwner(AccountHolder secondaryOwner);
}
