package com.jsblanco.springbanking.services.users;

import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.repositories.users.ThirdPartyRepository;
import com.jsblanco.springbanking.services.users.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Override
    public ThirdParty getById(Integer id) {
        Optional<ThirdParty> thirdParty = this.thirdPartyRepository.findById(id);
        if (thirdParty.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        return thirdParty.get();
    }

    @Override
    public ThirdParty save(ThirdParty account) {
        return this.thirdPartyRepository.save(account);
    }

    @Override
    public ThirdParty update(ThirdParty account) {
        Optional<ThirdParty> thirdParty = this.thirdPartyRepository.findById(account.getId());
        if (thirdParty.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        if (account.getPassword() == null)
            account.setPassword(thirdParty.get().getPassword());
        return this.thirdPartyRepository.save(account);
    }

    @Override
    public void delete(Integer id) {
        Optional<ThirdParty> thirdParty = this.thirdPartyRepository.findById(id);
        if (thirdParty.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such account exists in the database");
        this.thirdPartyRepository.delete(thirdParty.get());
    }

    @Override
    public List<ThirdParty> getAll() {
        return this.thirdPartyRepository.findAll();
    }

    @Override
    public List<ThirdParty> getByUsername(String name) {
        return this.thirdPartyRepository.getThirdPartiesByName(name);
    }
}
