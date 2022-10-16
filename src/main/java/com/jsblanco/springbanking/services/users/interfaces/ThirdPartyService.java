package com.jsblanco.springbanking.services.users.interfaces;

import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.services.users.interfaces.util.UserSubclassService;

public interface ThirdPartyService extends UserSubclassService<ThirdParty> {
    ThirdParty getByHashedKey(String hashedKey);
}
