package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

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
        assertThrows(IllegalArgumentException.class, () -> creditCard.setCreditLimit(maxCredit));
    }

    @DisplayName("Should use a default credit limit of 0.2, and should be able to be changed, but no lower than 0.1")
    @Test
    void setInterestRate() {
        assertEquals(creditCard.getDefaultInterestRate(), creditCard.getInterestRate(), "Should use default value when no specific rate is set");
        BigDecimal updatedRate = new BigDecimal("0.1");
        creditCard.setInterestRate(updatedRate);
        assertEquals(updatedRate, creditCard.getInterestRate(), "Should update interest rate when requested");
        assertThrows(IllegalArgumentException.class, () -> creditCard.setInterestRate(new BigDecimal("0.09")), "Should not set interest rates lower than 0.1");
    }

    @DisplayName("Should charge interest proportional to the months elapsed since last access")
    @Test
    void mustInterestBeCharged() {
        creditCard.setBalance(new Money(new BigDecimal("1000"), creditCard.getCurrency()));
        creditCard.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusDays(15).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1000"), creditCard.getCurrency()), creditCard.getBalance(), "Shouldn't apply interest if no month has elapsed");

        creditCard.setBalance(new Money(new BigDecimal("1000"), creditCard.getCurrency()));
        creditCard.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(1).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("900"), creditCard.getCurrency()), creditCard.getBalance(), "Should apply interest once if a month has elapsed");

        creditCard.setBalance(new Money(new BigDecimal("1000"), creditCard.getCurrency()));
        creditCard.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(2).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("810"), creditCard.getCurrency()), creditCard.getBalance(), "Should apply interest for as many months have elapsed");
    }
}
