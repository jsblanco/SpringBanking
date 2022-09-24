package com.jsblanco.springbanking.models.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public abstract class DateUtils {

    public static Period getPeriodBetweenDates(Date date1, Date date2) {
        return Period.between(getDateLocalValue(date1), getDateLocalValue(date2));
    }

    public static LocalDate getDateLocalValue(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
