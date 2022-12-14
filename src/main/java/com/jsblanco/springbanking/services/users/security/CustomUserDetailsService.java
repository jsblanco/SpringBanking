package com.jsblanco.springbanking.services.users.security;

import com.jsblanco.springbanking.config.CustomUserDetails;
import com.jsblanco.springbanking.models.users.User;

import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import com.jsblanco.springbanking.services.users.interfaces.AdminService;
import com.jsblanco.springbanking.services.users.interfaces.ThirdPartyService;
import com.jsblanco.springbanking.services.users.interfaces.util.UserSubclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    AdminService adminService;
    @Autowired
    ThirdPartyService thirdPartyService;
    @Autowired
    AccountHolderService accountHolderService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUser(getByUsername(username));

        return userDetails;
    }

    public User getByUsername(String username) {
        for (UserSubclassService<?> serviceInterface : new UserSubclassService[]{
                adminService,
                thirdPartyService,
                accountHolderService,
        }) {
            try {
                User user = (User) serviceInterface.getByUsername(username);
                if (user != null) return user;
            } catch (Exception ignored) {
            }
        }

        throw new UsernameNotFoundException("Could not find requested product in our database");
    }
}
