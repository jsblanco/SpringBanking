package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Integer> {
 AccountHolder getAccountHolderByUsername(@NotNull String username);
}
