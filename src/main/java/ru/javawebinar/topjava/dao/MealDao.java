package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealDao {

    private final List<Meal> meals;

    public MealDao() {
        this.meals = new CopyOnWriteArrayList<>(MealsUtil.MEALS);
    }

    public List<Meal> getAll() {
        return meals;
    }

    public void delete(Meal meal) {
        meals.remove(meal);
    }

    public void add(Meal meal) {
        meals.add(meal);
    }

    public void update(Meal meal) {
        for (Meal m : meals) {
            if (m.getId() == meal.getId()) {
                m.setCalories(meal.getCalories());
                m.setDateTime(meal.getDateTime());
                m.setDescription(meal.getDescription());
                break;
            }
        }
    }

    public Meal getById(int id) {
        return meals.stream().filter(meal -> meal.getId() == id).findFirst().orElse(null);
    }
}
