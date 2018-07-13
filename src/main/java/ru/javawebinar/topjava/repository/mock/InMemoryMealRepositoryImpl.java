package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int mealId) {
        Meal meal = repository.get(mealId);
        return userId == meal.getUserId() && repository.remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        Meal meal = repository.get(mealId);
        return userId == meal.getUserId()
                ? repository.get(mealId)
                : null;
    }

    @Override
    public Collection<Meal> getAll(int userId, LocalDate fromDate, LocalDate toDate) {
        return repository.values().stream()
                .filter(meal -> userId == meal.getUserId())
                .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), fromDate, toDate))
                .sorted(comparing(Meal::getDate, reverseOrder()))
                .collect(toList());
    }

}

