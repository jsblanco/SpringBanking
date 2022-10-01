package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.CheckingAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckingAccountRepository extends JpaRepository<CheckingAccount, Integer> {
    CheckingAccount getCheckingAccountById(Integer id);
    List<CheckingAccount> getCheckingAccountsByPrimaryOwner(AccountHolder primaryOwner);
    List<CheckingAccount> getCheckingAccountsBySecondaryOwner(AccountHolder secondaryOwner);
}
