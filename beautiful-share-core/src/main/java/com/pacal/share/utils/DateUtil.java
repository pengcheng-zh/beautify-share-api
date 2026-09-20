package com.pacal.share.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public final class DateUtil {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateTimeLongFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final DateTimeFormatter dateFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter dateNoYearFormatter = DateTimeFormatter.ofPattern( "MM-dd" );
    private static final DateTimeFormatter dateToMinuteFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public static Instant getPlusDayDate(Integer days) {
        return LocalDateTime.now().plusDays( days ).toInstant( ZoneOffset.UTC );
    }

    public static int getCurrentYear() {
        return LocalDate.now().getYear();
    }

    public static String getCurrentDate() {
        return LocalDateTime.now().format(dateFormatter);
    }

    public static String getPlusDate(Integer days) {
        return LocalDateTime.now().plusDays( days ).format( dateFormatter );
    }

    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(dateTimeFormatter);
    }
    public static String getCurrentLongTime() {
        return LocalDateTime.now().format(dateTimeLongFormatter);
    }
    public static String getCurrentLongTime(int minutes) {
        return LocalDateTime.now().plusMinutes( minutes ).format(dateTimeLongFormatter);
    }
    public static String getTimeToMinute(String sourceTime) {
        try {
            return LocalDateTime.parse( sourceTime, dateTimeFormatter ).format(dateToMinuteFormatter);
        } catch ( Exception ex ) {
            return "";
        }
    }
    public static String getPlusMinuteTime(String sourceTime, int minutes) {
        return LocalDateTime.parse( sourceTime, dateTimeFormatter ).plusMinutes( minutes ).format(dateTimeFormatter);
    }
    public static String getPlusSecondTime(String sourceTime, int seconds) {
        return LocalDateTime.parse( sourceTime, dateTimeFormatter ).plusSeconds( seconds ).format(dateTimeFormatter);
    }

    public static Long getTimestamp(String dateTime) {
        return LocalDateTime.parse( dateTime, dateTimeFormatter ).toEpochSecond( ZoneOffset.UTC );
    }
    public static String getTimeFromStamp(Long timestamp) {
        return LocalDateTime.ofInstant( Instant.ofEpochSecond( timestamp ), ZoneId.systemDefault() ).format(dateTimeFormatter);
    }

    public static String getDate(String dateTime) {
        return LocalDateTime.parse( dateTime, dateTimeFormatter ).format( dateFormatter );
    }

    public static boolean isDate(String date) {
        try {
           LocalDate localDate = LocalDate.parse( date,  dateFormatter);
           return true;
        } catch ( Exception ex ) {
            return false;
        }
    }
    public static String getShortDate(String dateTime) {
        return LocalDateTime.parse( dateTime, dateTimeFormatter ).format( dateNoYearFormatter );
    }

    public static Long getCurrentTimestamp() {
       return new Date().getTime() / 1000;
    }

    public static int getDaysBetween(String dateOne, String dateTwo) {
        if ( StringUtils.isEmpty( dateOne ) || StringUtils.isEmpty( dateTwo )) {
            return 0;
        }
        LocalDate startDate = LocalDate.parse( dateOne, dateFormatter );
        LocalDate endDate = LocalDate.parse( dateTwo, dateFormatter );
        return startDate.until( endDate ).getDays();
    }

    public static long getSecondsBetween(String beforeTime, String afterTime) {
        LocalDateTime before = LocalDateTime.now();
        if ( StringUtils.isNotEmpty( beforeTime ) ) {
            before = LocalDateTime.parse( beforeTime, dateTimeFormatter );
        }
        LocalDateTime after = LocalDateTime.now();
        if ( StringUtils.isNotEmpty( afterTime ) ) {
            after = LocalDateTime.parse( afterTime, dateTimeFormatter );
        }
        return ChronoUnit.SECONDS.between( before, after );
    }

    public static long getMinuteBetween(String beforeTime, String afterTime) {
        LocalDateTime before = LocalDateTime.now();
        if ( StringUtils.isNotEmpty( beforeTime ) ) {
            before = LocalDateTime.parse( beforeTime, dateTimeFormatter );
        }
        LocalDateTime after = LocalDateTime.now();
        if ( StringUtils.isNotEmpty( afterTime ) ) {
            after = LocalDateTime.parse( afterTime, dateTimeFormatter );
        }
        return ChronoUnit.MINUTES.between( before, after );
    }

    public static String getFirstDayOfWeek() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfWeek = today.with( DayOfWeek.MONDAY);
        return firstDayOfWeek.format(dateFormatter);
    }

    public static String getFirstDayOfMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfMonth = today.with( TemporalAdjusters.firstDayOfMonth() );
        return firstDayOfMonth.format(dateFormatter);
    }
}
