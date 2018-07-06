package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.List;

public class MealServiceImpl implements MealService {

    private final MealDao mealDao = new MealDao();

    @Override
    public List<MealWithExceed> getAll() {
        return MealsUtil.getFilteredWithExceeded(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
    }

    @Override
    public Meal getById(int id) {
        return mealDao.getById(id);
    }

    @Override
    public void create(Meal meal) {
        mealDao.add(meal);
    }

    @Override
    public void update(Meal meal) {
        mealDao.update(meal);
    }

    @Override
    public void delete(Meal meal) {
        mealDao.delete(meal);
    }
}
