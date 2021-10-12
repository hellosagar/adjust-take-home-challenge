package dev.sagar.adjusttakehomechallenge.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This class is contains the utility function related time.
 */
public class TimeUtil {

    public static long getCurrentTimeInMillisecond() {
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.getTime();
    }

    public static String getCurrentSecond() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ss", Locale.getDefault());
        Date currentTime = Calendar.getInstance().getTime();
        return dateFormat.format(currentTime.getTime());
    }

}
