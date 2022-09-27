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

class SavingsAccountTest {

    SavingsAccount savingsAccount;

    @BeforeEach
    void setUp() {
        savingsAccount = new SavingsAccount();
    }

    @Test
    void applyPenaltyIfNewBalanceIsBelowMinimum() {
        Money belowMinimumBalance = new Money(new BigDecimal("100"));
        savingsAccount.setMinimumBalance(belowMinimumBalance);
        savingsAccount.setBalance(belowMinimumBalance);
        savingsAccount.decreaseBalance(new Money(new BigDecimal(10)));

        Money expectedBalance = new Money(new BigDecimal("90"));
        expectedBalance.decreaseAmount(savingsAccount.getPenaltyFee());
        assertEquals(expectedBalance, savingsAccount.getBalance());
    }

    @Test
    void setInterestRate() {
        assertEquals(savingsAccount.getDefaultInterestRate(), savingsAccount.getInterestRate(), "Should use default value when no specific rate is set");
        BigDecimal updatedRate = new BigDecimal("0.1");
        savingsAccount.setInterestRate(updatedRate);
        assertEquals(updatedRate, savingsAccount.getInterestRate(), "Should update interest rate when requested");
        assertThrows(IllegalArgumentException.class, ()-> savingsAccount.setInterestRate(new BigDecimal("0.51")), "Should not set interest rates higher than 0.5");
    }

    @DisplayName("Should charge interest proportional to the years elapsed since last access")
    @Test
    void mustInterestBeCharged() {
        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(10).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Shouldn't apply interest if no year has elapsed");

        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusYears(1).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("997.50"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Should apply interest once if a year has elapsed");

        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusYears(2).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("995.01"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Should apply interest for as many years have elapsed");
    }
}
