package com.github.dgsc_fav.beinteractivetest.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by DG on 22.10.2016.
 */

public class TimeUtils {
    private static final SimpleDateFormat ISO_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String toIso(long timestamp) {
        TimeZone tz = TimeZone.getDefault();
        Calendar cal = GregorianCalendar.getInstance(tz);
        int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
        return ISO_1.format(timestamp + offsetInMillis);
    }
}
