package com.jsblanco.springbanking.models.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class HasInterestRateTest {

    HasInterestRate product = spy(HasInterestRate.class);

    @DisplayName("Should reduce balance by the interest rate for every overdue period")
    @Test
    void subtractInterest() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("0.10");

        assertEquals(amount, product.addInterest(amount, interestRate, 0), "Balance should not change if there are no overdue periods");
        assertEquals(new BigDecimal("1100.00"), product.addInterest(amount, interestRate, 1), "Should apply the interest rate once if there is only one overdue period");
        assertEquals(new BigDecimal("1210.00"), product.addInterest(amount, interestRate, 2), "If there are multiple overdue periods, interest rate should be applied sequentially for each");
    }
}
