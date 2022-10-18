package com.jsblanco.springbanking.controllers.products;

import com.jsblanco.springbanking.config.CustomUserDetails;
import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.dao.ThirdPartyTransferDao;
import com.jsblanco.springbanking.dao.TransferFundsDao;
import com.jsblanco.springbanking.models.interfaces.HasPeriodicChanges;
import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.services.products.interfaces.AccountService;
import com.jsblanco.springbanking.services.products.interfaces.BankProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

// Fachada principal para controlar todos los productos de forma centralizada.
@RestController
public class BankProductController {

    @Autowired
    BankProductService bankProductService;
    @Autowired
    AccountService accountService;

    @GetMapping("/product/")
    @ResponseStatus(HttpStatus.OK)
    public List<BankProduct> getAllUserBankProducts() {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.getByOwner((AccountHolder) user.getUser());
    }

    @GetMapping("/product/all")
    @ResponseStatus(HttpStatus.OK)
    public List<BankProduct> getAllBankProducts() {
        return this.bankProductService.getAll();
    }

    @GetMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankProduct getBankProductById(@PathVariable Integer id) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.getProductData(id, user.getUser());
    }

    @GetMapping("/product/balance/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Money getBankProductBalance(@PathVariable Integer id) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.bankProductService.getProductBalance(id, user.getUser());
    }

    @PostMapping("/product/")
    @ResponseStatus(HttpStatus.CREATED)
    public BankProduct saveBankProducts(@RequestBody CreateBankProductDao<BankProduct> dao) {
        return this.bankProductService.save(dao);
    }

    @PutMapping("/product/")
    @ResponseStatus(HttpStatus.CREATED)
    public BankProduct updateBankProducts(@RequestBody BankProduct product) {
        return this.bankProductService.update(product);
    }

    @PatchMapping("/product/maintenance/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public HasPeriodicChanges updateLastMaintenanceDate(@PathVariable Integer id, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return this.bankProductService.updateLastMaintenanceDate(id, date);
    }

    @PostMapping("/product/balance/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Money modifyBankProductBalance(@PathVariable Integer id, @RequestBody @Valid Money newBalance) {
        return this.bankProductService.modifyProductBalance(id, newBalance);
    }

    @PostMapping("/product/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Account> transferFundsBetweenProducts(@RequestBody @Valid TransferFundsDao dao) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.accountService.transferFunds(dao, user.getUser());
    }

    @PostMapping("/product/thirdparty")
    @ResponseStatus(HttpStatus.CREATED)
    public void thirdPartyOperation(@RequestHeader String hashedKey, @RequestBody @Valid ThirdPartyTransferDao dao) {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.accountService.thirdPartyOperation(hashedKey, dao, user.getUser());
    }

    @DeleteMapping("/product/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBankProducts(@PathVariable Integer id) {
        this.bankProductService.delete(id);
    }
}
