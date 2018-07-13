package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(int userId, Meal meal) {
        return repository.save(userId, meal);
    }

    @Override
    public void delete(int userId, int mealId) {
        checkNotFoundWithId(repository.delete(userId, mealId), mealId);
    }

    @Override
    public Meal get(int userId, int mealId) {
        return checkNotFoundWithId(repository.get(userId, mealId), mealId);
    }

    @Override
    public Collection<MealWithExceed> getAll(int userId, int caloriesPerDay, LocalDate fromDate, LocalDate toDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredWithExceeded(repository.getAll(userId, fromDate, toDate), caloriesPerDay, startTime, endTime);
    }
}