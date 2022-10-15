package com.jsblanco.springbanking.repositories.users;

import com.jsblanco.springbanking.models.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin getAdminByUsername(String name);
}
