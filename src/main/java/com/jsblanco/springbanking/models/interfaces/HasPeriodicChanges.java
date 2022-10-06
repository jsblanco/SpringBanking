package com.jsblanco.springbanking.models.interfaces;

import java.util.Date;

public interface HasPeriodicChanges {
    int getOverduePeriods(Date lastMaintenanceDate);
    Date getLastMaintenanceDate();
    void setLastMaintenanceDate(Date lastMaintenanceDate);
}
