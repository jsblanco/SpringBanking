package com.jsblanco.springbanking.services.users.interfaces.util;

import com.jsblanco.springbanking.services.utils.CrudServiceInterface;

public interface UserSubclassService<T> extends CrudServiceInterface<T> {

    T getByUsername(String name);
}
