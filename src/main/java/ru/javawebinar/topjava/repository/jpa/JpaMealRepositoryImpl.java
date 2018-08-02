package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User user = em.getReference(User.class, userId);
        meal.setUser(user);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return em.createNamedQuery(Meal.UPDATE)
                    .setParameter("description", meal.getDescription())
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("user", meal.getUser())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() != 0 ? meal : null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return em.createNamedQuery(Meal.FIND_BY_ID_AND_USER_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.GET_ALL_BY_USER_ID, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.GET_ALL_BY_USER_ID, Meal.class)
                .setParameter("userId", userId)
                .getResultStream()
                .filter(meal -> Util.isBetween(meal.getDateTime(), startDate, endDate))
                .collect(toList());
    }
}