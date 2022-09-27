package com.jsblanco.springbanking.models.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HasMaintenanceFeeTest {

    @DisplayName("Should reduce balance by the maintenance fee for every overdue period")
    @Test
    void subtractInterest() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal maintenanceFee = new BigDecimal("10");

        assertEquals(amount, HasMaintenanceFee.subtractMaintenance(amount, maintenanceFee, 0), "Balance should not change if there are no overdue periods");
        assertEquals(new BigDecimal("990.00"), HasMaintenanceFee.subtractMaintenance(amount, maintenanceFee, 1), "Should charge the maintenance fee once if there is only one overdue period");
        assertEquals(new BigDecimal("980.00"), HasMaintenanceFee.subtractMaintenance(amount, maintenanceFee, 2), "Should charge the maintenance fee as many times as there are overdue periods");
    }

}
