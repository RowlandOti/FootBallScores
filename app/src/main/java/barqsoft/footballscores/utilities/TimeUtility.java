package barqsoft.footballscores.utilities;

import android.content.Context;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.R;

/**
 * Created by Oti Rowland on 1/31/2016.
 */
public class TimeUtility {

    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.
        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else if (julianDay == currentJulianDay - 1) {
            return context.getString(R.string.yesterday);
        } else {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            String formatString = context.getString(R.string.date_format_dayoftheweek);
            SimpleDateFormat dayFormat = new SimpleDateFormat(formatString);
            return dayFormat.format(dateInMillis);
        }
    }

    public static String getToday(String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String todayDate = format.format(new Date());

        return todayDate;
    }
}
