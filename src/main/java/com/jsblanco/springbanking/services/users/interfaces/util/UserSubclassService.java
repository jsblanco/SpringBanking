package com.jsblanco.springbanking.services.users.interfaces.util;

import com.jsblanco.springbanking.services.utils.CrudServiceInterface;

import java.util.List;

public interface UserSubclassService<T> extends CrudServiceInterface<T> {

    List<T> getByUsername(String name);
}
