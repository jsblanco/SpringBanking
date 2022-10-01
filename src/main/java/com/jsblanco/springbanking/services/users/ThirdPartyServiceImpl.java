package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.repositories.users.ThirdPartyRepository;
import com.jsblanco.springbanking.services.users.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Override
    public ThirdParty getById(Integer id) {
        return this.thirdPartyRepository.getThirdPartyById(id);
    }

    @Override
    public ThirdParty save(ThirdParty account) {
        return this.thirdPartyRepository.save(account);
    }

    @Override
    public ThirdParty update(ThirdParty account) {
        if (this.thirdPartyRepository.getThirdPartyById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        return this.thirdPartyRepository.save(account);
    }

    @Override
    public void delete(ThirdParty account) {
        if (this.thirdPartyRepository.getThirdPartyById(account.getId()) == null)
            throw new IllegalArgumentException("No such account exists in the database");
        this.thirdPartyRepository.delete(account);
    }

    @Override
    public List<ThirdParty> getAll() {
        return this.thirdPartyRepository.findAll();
    }
}
