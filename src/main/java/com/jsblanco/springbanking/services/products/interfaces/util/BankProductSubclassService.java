package com.jsblanco.springbanking.services.products.interfaces.util;

import com.jsblanco.springbanking.models.users.AccountHolder;
import com.jsblanco.springbanking.services.utils.CrudServiceInterface;

import java.util.List;

public interface BankProductSubclassService<T> extends CrudServiceInterface<T> {

    List<T> getByOwner(AccountHolder owner);
}
