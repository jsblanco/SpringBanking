package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.services.products.interfaces.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CreditCardController {

    @Autowired
    CreditCardService creditCardService;

    @GetMapping("/card/")
    @ResponseStatus(HttpStatus.OK)
    public List<CreditCard> getAllCreditCards() {
        return this.creditCardService.getAll();
    }

    @GetMapping("/card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreditCard getCreditCardById(@PathVariable Integer id) {
        return this.creditCardService.getById(id);
    }

    @PostMapping("/card/")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard saveCreditCards(@RequestBody CreditCard card) {
        return this.creditCardService.save(card);
    }

    @PutMapping("/card/")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditCard updateCreditCards(@RequestBody CreditCard card) {
        return this.creditCardService.update(card);
    }

    @DeleteMapping("/card/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCreditCards(@PathVariable Integer id) {
        this.creditCardService.delete(id);
    }
}
