package com.jsblanco.springbanking.models.interfaces;

import com.jsblanco.springbanking.models.util.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public interface HasMaintenanceFee extends HasPeriodicCosts {

    Money getMonthlyMaintenanceFee();
    void setMonthlyMaintenanceFee(Money MaintenanceFee);
    void chargeMaintenanceIfApplies(Date lastAccess);

    static BigDecimal subtractMaintenance(BigDecimal balance, BigDecimal maintenanceFee, int overduePeriods) {
        BigDecimal maintenance = new BigDecimal(balance.toString());
        for (int i = 0; i < overduePeriods; i++)
            maintenance = maintenance.subtract(maintenanceFee);
        return maintenance.setScale(2, RoundingMode.HALF_EVEN);
    }
}
