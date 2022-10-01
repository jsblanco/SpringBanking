package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import com.jsblanco.springbanking.services.users.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin getById(Integer id) {
        return this.adminRepository.getAdminById(id);
    }

    @Override
    public Admin save(Admin account) {
        return this.adminRepository.save(account);
    }

    @Override
    public Admin update(Admin account) {
        if (this.adminRepository.getAdminById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.adminRepository.save(account);
    }

    @Override
    public void delete(Admin account) {
        if (this.adminRepository.getAdminById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.adminRepository.delete(account);
    }

    @Override
    public List<Admin> getAll() {
        return this.adminRepository.findAll();
    }
}
