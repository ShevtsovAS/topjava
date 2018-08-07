package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findFirstByIdAndUser_Id(int id, int userId);

    boolean deleteByIdAndUser_Id(int id, int userId);

    List<Meal> findAllByUser_IdOrderByDateTimeDesc(int userId);

    List<Meal> findAllByUser_IdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);
}
