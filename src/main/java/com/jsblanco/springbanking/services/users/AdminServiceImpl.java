package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.repositories.users.AdminRepository;
import com.jsblanco.springbanking.services.users.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired @Lazy
    PasswordEncoder passwordEncoder;
    @Autowired
    AdminRepository adminRepository;

    @Override
    public Admin getById(Integer id) {
        Optional<Admin> admin = this.adminRepository.findById(id);
        if (admin.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return admin.get();
    }

    @Override
    public Admin save(Admin account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return this.adminRepository.save(account);
    }

    @Override
    public Admin update(Admin account) {
        Optional<Admin> admin = this.adminRepository.findById(account.getId());
        if (admin.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        if (account.getPassword() == null)
            account.setPassword(admin.get().getPassword());
        return this.adminRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<Admin> admin = this.adminRepository.findById(id);
        if (admin.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.adminRepository.delete(admin.get());
    }

    @Override
    public List<Admin> getAll() {
        return this.adminRepository.findAll();
    }

    @Override
    public Admin getByUsername(String name) {
        return this.adminRepository.getAdminByUsername(name);
    }
}
