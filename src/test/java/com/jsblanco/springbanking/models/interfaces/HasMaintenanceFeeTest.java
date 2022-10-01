package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HasMaintenanceFeeTest {

    HasMaintenanceFee product = spy(HasMaintenanceFee.class);

    @BeforeEach
    void setUp() {
        when(product.getBalance()).thenReturn(new Money(new BigDecimal(1000)));
    }


    @DisplayName("Should reduce balance by the maintenance fee for every overdue period")
    @Test
    void subtractInterest() {
        BigDecimal maintenanceFee = new BigDecimal("10");

        assertEquals(new BigDecimal("1000.00"), product.subtractMaintenance(maintenanceFee, 0), "Balance should not change if there are no overdue periods");
        assertEquals(new BigDecimal("990.00"), product.subtractMaintenance(maintenanceFee, 1), "Should charge the maintenance fee once if there is only one overdue period");
        assertEquals(new BigDecimal("940.00"), product.subtractMaintenance(maintenanceFee, 6), "Should charge the maintenance fee as many times as there are overdue periods");
    }



}
