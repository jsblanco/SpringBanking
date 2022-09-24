package com.jsblanco.springbanking.controllers.users;

import com.jsblanco.springbanking.services.users.ThirdPartyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThirdPartyUserController {

    @Autowired
    ThirdPartyUserService thirdPartyUserService;
}
