package com.jsblanco.springbanking.repositories.products;

import com.jsblanco.springbanking.models.products.CreditCard;
import com.jsblanco.springbanking.models.users.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {
    CreditCard getCreditCardById(Integer id);
    List<CreditCard> getCreditCardsByPrimaryOwner(AccountHolder primaryOwner);
    List<CreditCard> getCreditCardsBySecondaryOwner(AccountHolder secondaryOwner);
}
