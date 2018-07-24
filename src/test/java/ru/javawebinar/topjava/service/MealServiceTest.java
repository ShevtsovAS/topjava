package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;
import static ru.javawebinar.topjava.util.MealsUtil.MEALS;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    private static final int MEAL_ID = START_SEQ + 2;

    @Autowired
    @Qualifier("mealServiceJdbcImpl")
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(MEAL_ID, USER_ID);
        assertThat(meal).isEqualTo(MEALS.get(0));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundException() {
        mealService.get(MEAL_ID, ADMIN_ID);
    }

    @Test
    public void delete() {
        mealService.delete(MEAL_ID, USER_ID);
        List<Meal> expectedMeals = MEALS.stream()
                .filter(meal -> meal.getId() != MEAL_ID)
                .sorted(comparing(Meal::getDateTime).reversed())
                .collect(toList());
        assertThat(mealService.getAll(USER_ID)).isEqualTo(expectedMeals);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundException() {
        mealService.delete(MEAL_ID, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        LocalDate filterDate = LocalDate.of(2015, Month.MAY, 30);
        List<Meal> actualMeals = mealService.getBetweenDates(filterDate, filterDate, USER_ID);
        List<Meal> expectedMeals = MEALS.stream()
                .filter(meal -> isBetween(meal.getDate(), filterDate, filterDate))
                .sorted(comparing(Meal::getDateTime).reversed())
                .collect(toList());
        assertThat(actualMeals).isEqualTo(expectedMeals);
    }

    @Test
    public void getBetweenDateTimes() {
        LocalDateTime startDateTime = LocalDateTime.of(2015, Month.MAY, 30, 10, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2015, Month.MAY, 30, 15, 0);
        List<Meal> actualMeals = mealService.getBetweenDateTimes(startDateTime, endDateTime, USER_ID);
        List<Meal> expectedMeals = MEALS.stream()
                .filter(meal -> isBetween(meal.getDateTime(), startDateTime, endDateTime))
                .sorted(comparing(Meal::getDateTime).reversed())
                .collect(toList());
        assertThat(actualMeals).isEqualTo(expectedMeals);
    }

    @Test
    public void getAll() {
        List<Meal> actualMeals = mealService.getAll(USER_ID);
        List<Meal> expectedMeals = MEALS.stream()
                .sorted(comparing(Meal::getDateTime).reversed())
                .collect(toList());
        assertThat(actualMeals).isEqualTo(expectedMeals);
    }

    @Test
    public void update() {
        Meal mealToUpdate = new Meal(MEALS.get(0));
        mealToUpdate.setCalories(100);
        mealToUpdate.setDescription("updated description");
        Meal actualMeal = mealService.update(mealToUpdate, USER_ID);
        assertThat(actualMeal).isEqualTo(mealToUpdate);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundException() {
        Meal mealToUpdate = new Meal(MEALS.get(0));
        mealToUpdate.setCalories(100);
        mealToUpdate.setDescription("updated description");
        mealService.update(mealToUpdate, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal mealToCreate = new Meal(LocalDateTime.now(), "create description", 1111);
        Meal createdMeal = mealService.create(mealToCreate, USER_ID);
        assertThat(createdMeal).isEqualToIgnoringGivenFields(mealToCreate, "id");
    }
}