package com.jsblanco.springbanking.models.interfaces;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HasInterestRateTest {

    @Test
    void subtractInterest() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal interestRate = new BigDecimal("0.10");

        assertEquals(amount, HasInterestRate.subtractInterest(amount, interestRate, 0));
        assertEquals(new BigDecimal("900.00"), HasInterestRate.subtractInterest(amount, interestRate, 1));
        assertEquals(new BigDecimal("810.00"), HasInterestRate.subtractInterest(amount, interestRate, 2));
    }
}
