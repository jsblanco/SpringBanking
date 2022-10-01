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

class CheckingAccountTest {

    CheckingAccount checkingAccount;

    @BeforeEach
    void setUp() {
        checkingAccount = new CheckingAccount();
    }

    @DisplayName("Should apply a penalty fee only when balance decreases to drop below minimum")
    @Test
    void decreaseBalance() {
        Money belowMinimumBalance = new Money(new BigDecimal("240"));
        checkingAccount.setBalance(belowMinimumBalance);
        checkingAccount.decreaseBalance(new Money(new BigDecimal(10)));

        Money expectedBalance = new Money(new BigDecimal("230"));
        expectedBalance.decreaseAmount(checkingAccount.getPenaltyFee());
        assertEquals(expectedBalance, checkingAccount.getBalance(), "Should apply a penalty when decreasing balance below min. value");

        checkingAccount.setBalance(expectedBalance);
        assertEquals(expectedBalance, checkingAccount.getBalance(), "Should not apply penalty if class is instantiated with balance below minimum");

        Money negativeBalance = new Money(new BigDecimal("-1000"));
        checkingAccount.setBalance(negativeBalance);
        assertEquals(negativeBalance, checkingAccount.getBalance(), "Balance should be possible to have a negative value");
    }

    @DisplayName("Should return as many overdue periods as months have elapsed since last access")
    @Test
    void getOverduePeriods() {
        assertEquals(0, checkingAccount.getOverduePeriods(Date.from(LocalDate.now().atStartOfDay().minusDays(27).toInstant(ZoneOffset.UTC))),
                "Should have no overdue periods if last access was less than a month ago");
        assertEquals(5, checkingAccount.getOverduePeriods(Date.from(LocalDate.now().atStartOfDay().minusMonths(5).toInstant(ZoneOffset.UTC))),
                "Should have as many overdue periods as months have passed since the last access");
        assertEquals(15, checkingAccount.getOverduePeriods(Date.from(LocalDate.now().atStartOfDay().minusMonths(15).toInstant(ZoneOffset.UTC))),
                "Should account for passed years when calculating passed months for the overdue periods");
    }

    @DisplayName("Should charge a flat maintenance fee for as many months as have elapsed since last access")
    @Test
    void chargeMaintenanceIfApplies() {
        checkingAccount.setBalance(new Money(new BigDecimal(1000)));

        checkingAccount.chargeMaintenanceIfApplies(Date.from(LocalDate.now().atStartOfDay().minusDays(27).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal(1000)), checkingAccount.getBalance(), "Shouldn't charge maintenance if less than a month has elapsed");

        checkingAccount.setBalance(new Money(new BigDecimal(1000)));
        checkingAccount.chargeMaintenanceIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(2).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal(976)), checkingAccount.getBalance(), "Should charge maintenance for as many months as have elapsed");

        checkingAccount.setBalance(new Money(new BigDecimal(1000)));
        checkingAccount.chargeMaintenanceIfApplies(Date.from(LocalDate.now().atStartOfDay().minusMonths(15).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal(820)), checkingAccount.getBalance(), "Should take elapsed years into account when calculating elapsed months for maintenance");
    }

    @DisplayName("Should use provided date to apply any pertinent balance changes and then set current date as last access")
    @Test
    void setLastAccess() {
        checkingAccount.setBalance(new Money(new BigDecimal("1000"), checkingAccount.getCurrency()));
        checkingAccount.setLastAccess(Date.from(LocalDate.now().atStartOfDay().minusMonths(1).toInstant(ZoneOffset.UTC)));
        assertEquals(new Money(new BigDecimal("988"), checkingAccount.getCurrency()), checkingAccount.getBalance(), "Should apply interest once if a month has elapsed");
        assertEquals(checkingAccount.getLastAccess(), DateUtils.today());
    }
}
