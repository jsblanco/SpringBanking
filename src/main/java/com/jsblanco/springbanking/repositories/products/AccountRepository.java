package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    List<Account> findAccountsByPrimaryOwner(AccountHolder primaryOwner, AccountHolder secondaryOwner);
    List<Account> findAccountsBySecondaryOwner(AccountHolder secondaryOwner);
}
