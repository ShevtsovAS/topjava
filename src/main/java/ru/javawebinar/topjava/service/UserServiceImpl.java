package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.model.UserRole;
import ru.javawebinar.topjava.repository.RoleRepository;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.jdbc.JdbcUserRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(@Autowired UserRepository repository, @Autowired(required = false) RoleRepository roleRepository) {
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        User savedUser = repository.save(user);
        if (repository instanceof JdbcUserRepositoryImpl) {
            saveRoles(savedUser);
        }
        return savedUser;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        User user = checkNotFoundWithId(repository.get(id), id);
        if (repository instanceof JdbcUserRepositoryImpl) {
            user.setRoles(getUserRoles(user));
        }
        return user;
    }

    private Set<Role> getUserRoles(User user) {
        Map<Integer, Set<Role>> roles = roleRepository.getAll().stream().collect(groupingBy(UserRole::getUserId, mapping(UserRole::getRole, toSet())));
        return roles.get(user.getId());
    }

    @Override
    @Transactional
    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        User savedUser = checkNotFound(repository.getByEmail(email), "email=" + email);
        if (repository instanceof JdbcUserRepositoryImpl) {
            savedUser.setRoles(getUserRoles(savedUser));
        }
        return savedUser;
    }

    @Cacheable("users")
    @Override
    @Transactional
    public List<User> getAll() {
        List<User> users = repository.getAll();
        if (repository instanceof JdbcUserRepositoryImpl) {
            users.forEach(user -> user.setRoles(getUserRoles(user)));
        }
        return users;
    }

    @CacheEvict(value = "users", allEntries = true)
    @Override
    @Transactional
    public void update(User user) {
        Assert.notNull(user, "user must not be null");
        User savedUser = checkNotFoundWithId(repository.save(user), user.getId());
        if (repository instanceof JdbcUserRepositoryImpl) {
            saveRoles(savedUser);
        }
    }

    private void saveRoles(User savedUser) {
        roleRepository.save(savedUser.getRoles()
                .stream()
                .map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(savedUser.getId());
                    userRole.setRole(role);
                    return userRole;
                })
                .collect(toSet()));
    }

    @Override
    public User getWithMeals(int id) {
        return checkNotFoundWithId(repository.getWithMeals(id), id);
    }
}