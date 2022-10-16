package com.jsblanco.springbanking.services.products.interfaces.util;

import com.jsblanco.springbanking.dao.CreateBankProductDao;
import com.jsblanco.springbanking.models.interfaces.HasPeriodicChanges;
import com.jsblanco.springbanking.models.products.Account;
import com.jsblanco.springbanking.models.products.BankProduct;
import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.services.users.interfaces.AccountHolderService;
import com.jsblanco.springbanking.services.utils.CrudServiceInterface;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.jsblanco.springbanking.models.util.Status.ACTIVE;
import static com.jsblanco.springbanking.util.DateUtils.today;

public interface BankProductSubclassService<T extends BankProduct> extends CrudServiceInterface<T> {

    T createNewProduct(CreateBankProductDao<T> dao);

    List<T> getByOwner(AccountHolder owner);

    default T populateBankProduct(CreateBankProductDao<T> dao, AccountHolderService accountHolderService) {
        T checkingAccount = dao.getProduct();
        if (checkingAccount instanceof Account) {
            ((Account) checkingAccount).setStatus(ACTIVE);
            ((Account) checkingAccount).setCreationDate(today());
        }
        if (checkingAccount instanceof HasPeriodicChanges) {
            ((HasPeriodicChanges) checkingAccount).setLastMaintenanceDate(today());
        }
        checkingAccount.setPrimaryOwner(accountHolderService.getById(dao.getPrimaryOwnerId()));
        try {
            if (dao.getSecondaryOwnerId() != null)
                checkingAccount.setSecondaryOwner(accountHolderService.getById(dao.getSecondaryOwnerId()));
        } catch (ResponseStatusException ignored) {
        }

        return checkingAccount;
    }

}
