package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import com.jsblanco.springbanking.services.products.interfaces.BankProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class BankProductController {

    @Autowired
    BankProductService bankProductService;
    @Autowired
    AccountService accountService;

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

    @GetMapping("/product/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getBankProductBalance(@PathVariable Integer id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.getProductBalance(id, user);
    }

    @PostMapping("/product/balance/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Money modifyBankProductBalance(@PathVariable Integer id, @RequestBody @Valid Money newBalance) {
        return this.bankProductService.modifyProductBalance(id, newBalance);
    }

    @PostMapping("/product/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Account> transferFundsBetweenProducts(@RequestBody @Valid TransferFundsDao dao) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.accountService.transferFunds(dao, user);
    }

    @PostMapping("/product/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public void thirdPartyOperation(@RequestHeader String hashedKey, @RequestBody @Valid ThirdPartyTransferDao dao) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.accountService.thirdPartyOperation(hashedKey, dao, user);
    }
}
