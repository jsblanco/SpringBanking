package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.ThirdPartyUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThirdPartyUserRepository extends JpaRepository<ThirdPartyUser, Integer> {
}
