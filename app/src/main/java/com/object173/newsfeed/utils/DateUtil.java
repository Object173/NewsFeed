package com.object173.newsfeed.utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.object173.newsfeed.R;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static Date getCropDate(final int cacheFrequency) {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DATE, -cacheFrequency);
        return calendar.getTime();
    }

    public static String getFullDateTime(Context context, Date date) {
        if(date == null) {
            return "";
        }
        return context.getString(R.string.date_time_format,
                DateFormat.getTimeFormat(context).format(date), DateFormat.getDateFormat(context).format(date));
    }
}
