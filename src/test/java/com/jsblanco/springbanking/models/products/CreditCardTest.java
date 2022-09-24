package com.jsblanco.springbanking.models.products;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CreditCardTest {

    CreditCard creditCard;

    @BeforeEach
    void setUp() {
        creditCard = new CreditCard();
    }

    @DisplayName("Should use default credit limit when none is set")
    @Test
    void setCreditLimit() {
        BigDecimal defaultCreditLimit = creditCard.getDefaultCreditLimit();
        assertEquals(defaultCreditLimit, creditCard.getCreditLimit().getAmount(), "Initial credit limit differs from default");
        BigDecimal updatedCreditLimit = defaultCreditLimit.add(new BigDecimal("1"));
        creditCard.setCreditLimit(updatedCreditLimit);
        assertEquals(updatedCreditLimit, creditCard.getCreditLimit().getAmount(), "Credit limit did not update upon calling setter");
    }

    @DisplayName("Should not admit credit limits over max value")
    @Test
    void setCreditLimitThrows() {
        BigDecimal maxCredit = creditCard.getMaxCreditLimit().add(new BigDecimal("100"));
        assertThrows(IllegalArgumentException.class, ()->creditCard.setCreditLimit(maxCredit));
    }

    @DisplayName("")
    @Test
    void setInterestRate() {
    }

    @DisplayName("")
    @Test
    void mustInterestBeCharged() {
    }

    @DisplayName("")
    @Test
    void calculateInterest() {
    }

    @DisplayName("")
    @Test
    void testSetCreditLimit() {
    }

    @DisplayName("")
    @Test
    void setLastAccess() {
    }
}
