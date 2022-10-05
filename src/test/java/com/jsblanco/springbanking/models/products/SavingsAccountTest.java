package com.jsblanco.springbanking.models.products;

import com.jsblanco.springbanking.models.util.DateUtils;
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

    @DisplayName("Should apply a penalty fee only when balance decreases to drop below minimum")
    @Test
    void decreaseBalance() {
        Money belowMinimumBalance = new Money(new BigDecimal("1000"));
        savingsAccount.setMinimumBalance(belowMinimumBalance);
        savingsAccount.setBalance(belowMinimumBalance);
        savingsAccount.decreaseBalance(new Money(new BigDecimal(10)));

        Money expectedBalance = new Money(new BigDecimal("990"));
        expectedBalance.decreaseAmount(savingsAccount.getPenaltyFee());
        assertEquals(expectedBalance, savingsAccount.getBalance(), "Should apply a penalty when decreasing balance below min. value");

        savingsAccount.setBalance(expectedBalance);
        assertEquals(expectedBalance, savingsAccount.getBalance(), "Should not apply penalty if class is instantiated with balance below minimum");

        assertThrows(IllegalArgumentException.class, () -> savingsAccount.setBalance(new Money(new BigDecimal("90"))), "Balance cannot be set below 100");
    }

    @DisplayName("Should use a default credit limit of 0.0025, and should be able to be changed, but not higher than 0.5 and not lower than 0")
    @Test
    void setInterestRate() {
        assertEquals(savingsAccount.getDefaultInterestRate(), savingsAccount.getInterestRate(), "Should use default value when no specific rate is set");
        BigDecimal updatedRate = new BigDecimal("0.1000");
        savingsAccount.setInterestRate(updatedRate);
        assertEquals(updatedRate, savingsAccount.getInterestRate(), "Should update interest rate when requested");
        assertThrows(IllegalArgumentException.class, () -> savingsAccount.setInterestRate(new BigDecimal("0.51")), "Should not set interest rates higher than 0.5");
        assertThrows(IllegalArgumentException.class, () -> savingsAccount.setInterestRate(new BigDecimal("-0.1")), "Should not set interest rates lower than 0");
    }

    @DisplayName("Should add interest proportional to the years elapsed since last access")
    @Test
    void mustInterestBeCharged() {
        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(10).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Shouldn't apply interest if no year has elapsed");

        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusYears(1).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1002.50"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Should apply interest once if a year has elapsed");

        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.chargeInterestIfApplies(Date.from(LocalDate.now().atStartOfDay().minusYears(2).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1005.01"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Should apply interest for as many years have elapsed");
    }

    @DisplayName("Should use provided date to apply any pertinent balance changes and then set current date as last access")
    @Test
    void setLastAccess() {
        savingsAccount.setBalance(new Money(new BigDecimal("1000"), savingsAccount.getCurrency()));
        savingsAccount.setLastAccess(Date.from(LocalDate.now().atStartOfDay().minusYears(1).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("1002.50"), savingsAccount.getCurrency()), savingsAccount.getBalance(), "Should apply interest once if a year has elapsed");
        assertEquals(savingsAccount.getLastAccess(), DateUtils.today());
    }
}
