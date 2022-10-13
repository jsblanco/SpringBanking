package com.jsblanco.springbanking.controllers.users;

import com.jsblanco.springbanking.models.users.ThirdParty;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.services.users.interfaces.ThirdPartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ThirdPartyController {

    @Autowired
    ThirdPartyService thirdPartyService;

    @GetMapping("/thirdparty/")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> getAllThirdParties() {
        return this.thirdPartyService.getAll();
    }

    @GetMapping("/thirdparty/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ThirdParty getThirdPartyById(@PathVariable Integer id) {
        return this.thirdPartyService.getById(id);
    }

    @PostMapping("/thirdparty/")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty saveThirdParty(@RequestBody ThirdParty thirdParty) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.thirdPartyService.save(thirdParty, user);
    }

    @PutMapping("/thirdparty/")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty updateThirdParty(@RequestBody ThirdParty thirdParty) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.thirdPartyService.update(thirdParty, user);
    }

    @DeleteMapping("/thirdparty/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteThirdParty(@PathVariable Integer id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.thirdPartyService.delete(id, user);
    }

}
