package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

public interface MealService {

    List<MealWithExceed> getAll();
    Meal getById(int id);
    void create(Meal meal);
    void update(Meal meal);
    void delete(Meal meal);

}
