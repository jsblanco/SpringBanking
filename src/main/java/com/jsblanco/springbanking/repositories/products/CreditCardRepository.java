package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
}
