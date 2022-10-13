package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.services.products.interfaces.BankProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankProductController {

    @Autowired
    BankProductService bankProductService;

    @GetMapping("/product/")
    @ResponseStatus(HttpStatus.OK)
    public List<BankProduct> getAllBankProducts() {
        return this.bankProductService.getAll();
    }

    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankProduct getBankProductById(@PathVariable Integer id) {
        return this.bankProductService.get(id);
    }

    @GetMapping("/product/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getBankProductBalance(@PathVariable Integer id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.getProductBalance(id, user);
    }

    @PostMapping("/product/{id}/balance")
    @ResponseStatus(HttpStatus.CREATED)
    public Money modifyBankProductBalance(@PathVariable Integer id, @RequestBody Money newBalance) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.modifyProductBalance(id, newBalance, user);
    }

    @PostMapping("/product/")
    @ResponseStatus(HttpStatus.CREATED)
    public BankProduct saveBankProducts(@RequestBody BankProduct product) {
        return this.bankProductService.save(product);
    }

    @PostMapping("/product/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public List<BankProduct> transferFundsBetweenProducts(@RequestBody TransferFundsDao dao) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.transferFunds(dao, user);
    }

    @PostMapping("/product/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public void thirdPartyOperation(@RequestHeader String hashedKey, @RequestBody ThirdPartyTransferDao dao) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.bankProductService.thirdPartyOperation(hashedKey, dao, user);
    }

    @PutMapping("/product/")
    @ResponseStatus(HttpStatus.CREATED)
    public BankProduct updateBankProducts(@RequestBody BankProduct product) {
        return this.bankProductService.update(product);
    }

    @DeleteMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBankProducts(@PathVariable Integer id) {
        this.bankProductService.delete(id);
    }
}
