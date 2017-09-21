package com.ian.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import java.util.Date;
import java.util.Locale;

public class DateCalculator {

    public static void main(String[] args) {
        System.out.println(getDiffYears(dateFromString("1991-06-25"), new Date()));
    }

    public static int getDiffYears(Date first, Date last) {
        Calendar a = null, b = null;
        if (first != null && last != null) {
            a = getCalendar(first);
            b = getCalendar(last);
        } else {
            a = getCalendar(new Date());
            b = getCalendar(new Date());
        }
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH)
                || (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }

    public static Date dateFromString(String startDateString) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        try {
            startDate = df.parse(startDateString);
            String newDateString = df.format(startDate);
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }
}
