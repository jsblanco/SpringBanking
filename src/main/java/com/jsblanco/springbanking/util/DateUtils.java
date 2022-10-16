package com.jsblanco.springbanking.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Period getPeriodBetweenDates(Date date1, Date date2) {
        return Period.between(getDateLocalValue(date1), getDateLocalValue(date2));
    }

    public static LocalDate getDateLocalValue(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date getDateFromLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static Date today(){
        return round(Date.from(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC)));
    }

    public static Date round(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int deltaMin = calendar.get(Calendar.SECOND)/30;

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MINUTE, deltaMin);

        return calendar.getTime();
    }
}
