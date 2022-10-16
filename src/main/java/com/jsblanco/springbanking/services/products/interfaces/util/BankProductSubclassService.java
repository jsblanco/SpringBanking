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
        T product = dao.getProduct();
        if (product.getCreationDate() == null) product.setCreationDate(today());
        if (product instanceof Account && ((Account) product).getStatus() == null)
            ((Account) product).setStatus(ACTIVE);
        if (product instanceof HasPeriodicChanges)
            ((HasPeriodicChanges) product).setLastMaintenanceDate(product.getCreationDate());

        product.setPrimaryOwner(accountHolderService.getById(dao.getPrimaryOwnerId()));

        try {
            if (dao.getSecondaryOwnerId() != null)
                product.setSecondaryOwner(accountHolderService.getById(dao.getSecondaryOwnerId()));
        } catch (ResponseStatusException ignored) {
        }

        return product;
    }

}
