package ru.javawebinar.topjava.model;

import lombok.Getter;
import lombok.Setter;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {

    @Getter
    private final int id;

    @Getter
    @Setter
    private LocalDateTime dateTime;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private int calories;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.id = MealsUtil.getId();
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }
}
