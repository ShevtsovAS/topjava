package ru.javawebinar.topjava.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class TimeUtil {
    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }
}
