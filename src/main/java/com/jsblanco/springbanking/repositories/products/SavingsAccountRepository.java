package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.SavingsAccount;
import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Integer> {
    SavingsAccount getSavingsAccountById(Integer id);
    List<SavingsAccount> getSavingsAccountsByPrimaryOwner(AccountHolder primaryOwner);
    List<SavingsAccount> getSavingsAccountsBySecondaryOwner(AccountHolder secondaryOwner);
}
