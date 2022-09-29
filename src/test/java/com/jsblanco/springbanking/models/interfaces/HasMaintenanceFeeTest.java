package com.jsblanco.springbanking.models.interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HasMaintenanceFeeTest {

    HasMaintenanceFee product = spy(HasMaintenanceFee.class);

    @DisplayName("Should reduce balance by the maintenance fee for every overdue period")
    @Test
    void subtractInterest() {
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal maintenanceFee = new BigDecimal("10");

        assertEquals(amount, product.subtractMaintenance(amount, maintenanceFee, 0), "Balance should not change if there are no overdue periods");
        assertEquals(new BigDecimal("990.00"), product.subtractMaintenance(amount, maintenanceFee, 1), "Should charge the maintenance fee once if there is only one overdue period");
        assertEquals(new BigDecimal("940.00"), product.subtractMaintenance(amount, maintenanceFee, 6), "Should charge the maintenance fee as many times as there are overdue periods");
    }



}
