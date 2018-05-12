package ru.javawebinar.topjava.model;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ToString
public class UserMealWithExceed {
    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean exceed;
}