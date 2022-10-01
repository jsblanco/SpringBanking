package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.BankProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankProductRepository extends JpaRepository<BankProduct, Integer> {
    BankProduct findBankProductById(Integer id);
}
