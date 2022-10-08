package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    List<Admin> getAdminsByName(String name);
}
