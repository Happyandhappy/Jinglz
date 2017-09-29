package com.jinglz.app.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    private static final SimpleDateFormat SERVER_DATE_FORMATTER =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);

    private static final SimpleDateFormat TIME_DATE_FORMATTER =
            new SimpleDateFormat("hh:mm a", Locale.US);

    private static final SimpleDateFormat TIMER_DATE_FORMATTER =
            new SimpleDateFormat("mm:ss", Locale.getDefault());

    private static final SimpleDateFormat DAY_NAME_FORMATTER =
            new SimpleDateFormat("E", Locale.US);

    private static final SimpleDateFormat MONTH_NAME_FORMATTER =
            new SimpleDateFormat("MMMM", Locale.US);

    static {
        final TimeZone serverTimeZone = TimeZone.getTimeZone("UTC");
       // final TimeZone clientTimeZone = TimeZone.getTimeZone("EST");
        //final TimeZone clientTimeZone = TimeZone.getTimeZone("EST");

        Calendar calendar = Calendar.getInstance();
        TimeZone clientTimeZone = calendar.getTimeZone();

        SERVER_DATE_FORMATTER.setTimeZone(serverTimeZone);

        TimeZone.setDefault(clientTimeZone);

        TIME_DATE_FORMATTER.setTimeZone(clientTimeZone);
        TIMER_DATE_FORMATTER.setTimeZone(clientTimeZone);
        DAY_NAME_FORMATTER.setTimeZone(clientTimeZone);
        MONTH_NAME_FORMATTER.setTimeZone(clientTimeZone);
    }

    /**
     * Returns {@code Date} by parsing text in {@see SERVER_DATE_FORMATTER}.
     * @param date String contains date to parse
     * @return Date if {@code date} is not null.
     * @exception ParseException  if error has been reached unexpectedly while parsing
     */
    @Nullable
    public static Date fromServerFormat(@Nullable String date) {
        if (date == null) {
            return null;
        }
        try {
            return SERVER_DATE_FORMATTER.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns difference between two dates in milli seconds.
     *
     * @param date1 Date to be subtracted
     * @param date2 Date from which {@code date1} will be subtracted
     * @return difference between times
     */
    public static long subtractDatesInMillis(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }

    /**
     * Return string after formatting {@code date} using {@see TIME_DATE_FORMATTER}.
     *
     * @param date Date to format
     * @return formatted string
     */
    public static String toTimeFormat(Date date) {
        return TIME_DATE_FORMATTER.format(date).toLowerCase(Locale.US);
    }

    /**
     * Returns formatted string from specified date. it uses {@see TIMER_DATE_FORMATTER}
     * to format long object in string.
     *
     * @param date long type variable to format
     * @return formatted string
     */
    public static String toTimerFormat(long date) {
        return TIMER_DATE_FORMATTER.format(date);
    }

    /**
     * Returns string after formatting date in {@see SERVER_DATE_FORMATTER} format.
     *
     * @param date long type variable to format
     * @return formatted string
     */
    public static String toServerFormat(long date) {
        return SERVER_DATE_FORMATTER.format(date).toLowerCase(Locale.US);
    }

    /**
     * Returns name of default time zone.
     *
     * @return string containing time zone
     */
    public static String getTimeZoneDisplayName() {
        return TimeZone.getDefault().getID();
    }

    /**
     * Return time zone offset value. it can be retrieved by calling
     * method {@link TimeZone#getOffset(long)}
     *
     * @return offset value
     */
    public static long getTimeZoneOffset() {
        final TimeZone mTimeZone = TimeZone.getDefault();
        return mTimeZone.getOffset(new Date().getTime()) / 1000 / 60;
    }

    /**
     * Returns time in milliseconds after setting given calender fields to given
     * values.
     *
     * @return time in milliseconds
     */
    public static long getStartOfDay() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * Returns name of day at specified date. string can be format according to
     * {@see DAY_NAME_FORMATTER} format.
     *
     * @param date Date to set.
     * @return formatted string containing day name.
     */
    public static String getDayName(Date date) {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(date);
        return DAY_NAME_FORMATTER.format(calendar.getTime()).substring(0, 2);
    }

    /**
     * Returns week number in specified date.
     *
     * @param date Date to set
     * @return week number
     */
    public static int getWeekOfMonth(Date date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * Returns month name after formatting it using {@see MONTH_NAME_FORMATTER}.
     * @return formatted string containing month
     */
    public static String getCurrentMonthName() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        return MONTH_NAME_FORMATTER.format(calendar.getTime());
    }

}
