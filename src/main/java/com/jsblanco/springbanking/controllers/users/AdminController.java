package com.jsblanco.springbanking.controllers.users;

import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.services.users.interfaces.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/admin/")
    @ResponseStatus(HttpStatus.OK)
    public List<Admin> getAllAdmins() {
        return this.adminService.getAll();
    }

    @GetMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Admin getAdminById(@PathVariable Integer id) {
        return this.adminService.getById(id);
    }

    @PostMapping("/admin/")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin saveAdmin(@RequestBody Admin admin) {
        return this.adminService.save(admin);
    }

    @PutMapping("/admin/")
    @ResponseStatus(HttpStatus.CREATED)
    public Admin updateAdmin(@RequestBody Admin admin) {
        return this.adminService.update(admin);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdmin(@PathVariable Integer id) {
        this.adminService.delete(id);
    }
}
