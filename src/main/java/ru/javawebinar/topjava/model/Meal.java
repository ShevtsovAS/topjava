package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * GKislin
 * 11.01.2015.
 */
@NamedQueries(value = {
        @NamedQuery(name = Meal.UPDATE, query = "UPDATE Meal meal " +
                "SET meal.description=:description, " +
                "meal.dateTime=:dateTime, " +
                "meal.calories=:calories, " +
                "meal.user=:user " +
                "where meal.id=:id and meal.user.id=:userId"),
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal meal WHERE meal.id=:id and meal.user.id=:userId"),
        @NamedQuery(name = Meal.FIND_BY_ID_AND_USER_ID, query = "SELECT Meal FROM Meal meal WHERE meal.id=:id and meal.user.id=:userId"),
        @NamedQuery(name = Meal.GET_ALL_BY_USER_ID, query = "SELECT Meal FROM Meal meal WHERE meal.user.id=:userId ORDER BY meal.dateTime DESC"),
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id, datetime", name = "user_date_time_idx")})
public class Meal extends AbstractBaseEntity {

    public static final String UPDATE = "Meal.update";
    public static final String DELETE = "Meal.delete";
    public static final String FIND_BY_ID_AND_USER_ID = "Meal.findByIdAndUserId";
    public static final String GET_ALL_BY_USER_ID = "Meal.getAllByUserId";


    @Column(name = "datetime", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotNull
    private String description;

    @Column(name = "calories", nullable = false, columnDefinition = "integer default 500")
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = User.class, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
