package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class HasMinimumBalanceTest {

    HasMinimumBalance product = spy(HasMinimumBalance.class);

    @BeforeEach
    void setUp() {
        when(product.getMinimumAmount()).thenReturn(new BigDecimal(600));
    }

    @DisplayName("Should only charge the penalty fee if the balance falls below minimum")
    @Test
    void reduceBalanceAccountingForPenalty() {
        assertEquals(new Money(new BigDecimal(600)), product.reduceBalanceAccountingForPenalty(new Money(new BigDecimal(1000)), new Money(new BigDecimal(400)), new BigDecimal(40)), "Should not charge penalty fee if final balance is above minimum");
        assertEquals(new Money(new BigDecimal(360)), product.reduceBalanceAccountingForPenalty(new Money(new BigDecimal(1000)), new Money(new BigDecimal(600)), new BigDecimal(40)), "Should charge penalty fee when final balance is below minimum");
    }
}
