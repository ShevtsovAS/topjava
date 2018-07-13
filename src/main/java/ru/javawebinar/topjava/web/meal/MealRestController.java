package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        log.info("save {}", meal);
        return service.save(authUserId(), meal);
    }

    public void delete(int mealId) {
        log.info("delete {}", mealId);
        service.delete(authUserId(), mealId);
    }

    public Meal get(int mealId) {
        log.info("get {}", mealId);
        return service.get(authUserId(), mealId);
    }

    public Collection<MealWithExceed> getAll(LocalDate fromDate, LocalDate toDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAll from date {}, to date {}, start time {}, end time {}", fromDate, toDate, startTime, endTime);
        return service.getAll(authUserId(), authUserCaloriesPerDay(), fromDate, toDate, startTime, endTime);
    }
}