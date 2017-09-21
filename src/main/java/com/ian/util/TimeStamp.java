package com.ian.util;

import java.sql.Timestamp;
import java.util.Date;

public class TimeStamp {

    public static String getTimeStamp() {
        Date date = new Date();
        //remove .
        String str1 = new Timestamp(date.getTime()).toString();
        String str2 = str1.replace(".", "");
        //replace :
        String str3 = str2.replace(":", "");
        //replace -
        String str4 = str3.replace("-", "");
        //replace " "
        String str5 = str4.replace(" ", "");
        return str5;
    }
}
