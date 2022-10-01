package com.jsblanco.springbanking.services.utils;

import java.util.List;

public interface CrudServiceInterface<T> {
    T getById(Integer id);
    T save(T account);
    T update(T account);
    void delete(T account);
    List<T> getAll();
}
