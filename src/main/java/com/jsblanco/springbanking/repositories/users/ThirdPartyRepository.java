package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.ThirdParty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Integer> {
    ThirdParty getThirdPartyByUsername(@NotNull String username);
    ThirdParty getThirdPartyByHashedKey(@NotNull String hashedKey);
}
