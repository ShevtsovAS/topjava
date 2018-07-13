package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public interface MealService {
    Meal save(int userId, Meal meal);

    void delete(int userId, int mealId);

    Meal get(int userId, int mealId);

    Collection<MealWithExceed> getAll(int userId, int caloriesPerDay, LocalDate fromDate, LocalDate toDate, LocalTime startTime, LocalTime endTime);
}