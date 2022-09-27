package com.jsblanco.springbanking.models.interfaces;

import java.util.Date;

public interface HasPeriodicCosts {
    int getOverduePeriods(Date lastAccess);
    Date getLastAccess();
    void setLastAccess(Date lastAccess);
}
