package com.jsblanco.springbanking.models.interfaces;

import javax.validation.constraints.NotNull;
import java.util.Date;

public interface HasPeriodicChanges {
    int getOverduePeriods(Date lastMaintenanceDate);
    Date getLastMaintenanceDate();
    void setLastMaintenanceDate(@NotNull Date lastMaintenanceDate);
}
