package com.jsblanco.springbanking.services.users.interfaces;

import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.services.users.interfaces.util.UserSubclassService;

public interface ThirdPartyService extends UserSubclassService<ThirdParty> {
    ThirdParty save(ThirdParty account, User loggedUser);
    ThirdParty update(ThirdParty account, User loggedUser);
    void delete(Integer accountId, User loggedUser);
}
