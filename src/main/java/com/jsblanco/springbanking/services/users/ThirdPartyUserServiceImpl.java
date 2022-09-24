package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.repositories.users.ThirdPartyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdPartyUserServiceImpl implements ThirdPartyUserService {

    @Autowired
    ThirdPartyUserRepository thirdPartyUserRepository;
}
