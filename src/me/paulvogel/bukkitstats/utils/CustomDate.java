package me.paulvogel.bukkitstats.utils;

/**
 * Created by Paul on 13.02.2015.
 */
public class CustomDate {

    private static int day, month, year, hour, minute, second;

    public CustomDate(final int day, final int month, final int year, final int hour, final int minute, final int second) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

}