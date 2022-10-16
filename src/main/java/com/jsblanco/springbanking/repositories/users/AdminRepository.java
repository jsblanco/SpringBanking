package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin getAdminByUsername(@NotNull String username);
}
