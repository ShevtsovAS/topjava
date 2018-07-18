package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static boolean isBetween(LocalDate ld, LocalDate fromDate, LocalDate toDate) {
        return ld.compareTo(fromDate) >= 0 && ld.compareTo(toDate) <= 0;
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate getDate(String date) {
        return Optional.ofNullable(date).map(LocalDate::parse).orElse(null);
    }

    public static LocalTime getTime(String time) {
        return Optional.ofNullable(time).map(LocalTime::parse).orElse(null);
    }
}
