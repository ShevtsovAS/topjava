package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.UserRole;

import java.util.List;
import java.util.Set;

public interface RoleRepository {

    List<UserRole> getAll();

    void save(Set<UserRole> userRoles);

}
