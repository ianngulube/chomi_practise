/*
This code was written by Ian Ngulube, Software Developer at Biza Telecoms.
 */
package com.ian.util;

import java.util.Date;

public class DateFetcher {

    public static Date getDate(String x) {
        x = x.substring(0, 4) + "/" + x.substring(4, x.length());
        x = x.substring(0, 7) + "/" + x.substring(7, x.length());
        return new Date(x);
    }
}
