package com.jsblanco.springbanking.services.products;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.products.*;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.models.users.Admin;
import com.jsblanco.springbanking.models.users.User;
import com.jsblanco.springbanking.models.util.Money;
import com.jsblanco.springbanking.services.products.interfaces.*;
import com.jsblanco.springbanking.services.products.interfaces.util.BankProductSubclassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class BankProductServiceImpl implements BankProductService {

    @Autowired
    CreditCardService creditCardService;
    @Autowired
    SavingsAccountService savingsAccountService;
    @Autowired
    CheckingAccountService checkingAccountService;
    @Autowired
    StudentCheckingAccountService studentCheckingAccountService;
    @Autowired
    AccountService accountService;

    @Override
    public BankProduct get(Integer id) {

        for (BankProductSubclassService<?> repo : new BankProductSubclassService[]{
                creditCardService,
                checkingAccountService,
                savingsAccountService,
                studentCheckingAccountService}) {
            try {
                BankProduct product = repo.getById(id);
                if (product != null) return product;
            } catch (Exception ignored) {
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such product exists in the database");
    }

    @Override
    public BankProduct save(CreateBankProductDao dao) {
        try {
            get(dao.getProduct().getId());
        } catch (Exception e) {
            if (dao.getProduct() instanceof CheckingAccount)
                return accountService.createCheckingAccount(dao);
            return fetchProductService(dao.getProduct()).createNewProduct(dao);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already a product with this ID inside the database");
    }

    @Override
    public BankProduct update(BankProduct bankProduct) {
        try {
            BankProduct dbProduct = get(bankProduct.getId());
            if (bankProduct.getPrimaryOwner() == null)
                bankProduct.setPrimaryOwner(dbProduct.getPrimaryOwner());
            if (bankProduct.getSecondaryOwner() == null && dbProduct.getSecondaryOwner() != null)
                bankProduct.setSecondaryOwner(dbProduct.getSecondaryOwner());
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such product exists in the database");
        }
        return (BankProduct) fetchProductService(bankProduct).save(bankProduct);
    }

    @Override
    public void delete(Integer id) {
        BankProduct productInDb = get(id);
        if (productInDb == null)
            throw new IllegalArgumentException("No such account in the db");
        fetchProductService(productInDb).delete(id);
    }

    @Override
    public BankProduct getProductData(Integer id, User user) {
        BankProduct product = this.get(id);
        if (user instanceof Admin || product.isOwnedBy((AccountHolder) user))
            return product;
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account data can only be consulted by its owners.");
    }

    @Override
    public Money getProductBalance(Integer id, User user) {
        BankProduct product = get(id);
        if (user instanceof Admin || (user instanceof AccountHolder && product.isOwnedBy((AccountHolder) user))) {
            return get(id).getBalance();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User does not have access to this account's balance");
    }

    @Override
    public Money modifyProductBalance(Integer id, Money balanceChange) {
        BankProduct product = get(id);
        if (balanceChange.getAmount().signum() > 0)
            product.increaseBalance(balanceChange);
        else
            product.decreaseBalance(balanceChange);
        return update(product).getBalance();
    }

    @Override
    public List<BankProduct> getAll() {
        Set<BankProduct> accounts = new HashSet<>();

        accounts.addAll(creditCardService.getAll());
        accounts.addAll(savingsAccountService.getAll());
        accounts.addAll(checkingAccountService.getAll());
        accounts.addAll(studentCheckingAccountService.getAll());

        return accounts.stream().toList();
    }

    @Override
    public List<BankProduct> getByOwner(AccountHolder owner) {
        Set<BankProduct> accounts = new HashSet<>();

        accounts.addAll(creditCardService.getByOwner(owner));
        accounts.addAll(checkingAccountService.getByOwner(owner));
        accounts.addAll(studentCheckingAccountService.getByOwner(owner));
        accounts.addAll(savingsAccountService.getByOwner(owner));

        return accounts.stream().toList();
    }

    private BankProductSubclassService fetchProductService(BankProduct bankProduct) {
        if (CreditCard.class.equals(bankProduct.getClass())) {
            return this.creditCardService;
        }
        if (CheckingAccount.class.equals(bankProduct.getClass())) {
            return this.checkingAccountService;
        }
        if (SavingsAccount.class.equals(bankProduct.getClass())) {
            return this.savingsAccountService;
        }
        if (StudentCheckingAccount.class.equals(bankProduct.getClass())) {
            return this.studentCheckingAccountService;
        }

        throw new IllegalArgumentException("Product not recognised");
    }
}
