package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.UserRole;
import ru.javawebinar.topjava.repository.RoleRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(readOnly = true)
public class JdbcRoleRepositoryImpl implements RoleRepository {

    private static final BeanPropertyRowMapper<UserRole> ROW_MAPPER = BeanPropertyRowMapper.newInstance(UserRole.class);

    private final JdbcTemplate jdbcTemplate;

    public JdbcRoleRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserRole> getAll() {
        return jdbcTemplate.query("SELECT * FROM user_roles ORDER BY user_id", ROW_MAPPER);
    }

    @Override
    @Transactional
    public void save(Set<UserRole> userRoles) {

        List<UserRole> userRoleList = new ArrayList<>(userRoles);
        userRoleList.removeAll(getAll());

        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                UserRole userRole = userRoleList.get(i);
                ps.setInt(1, userRole.getUserId());
                ps.setString(2, userRole.getRole().toString());
            }

            @Override
            public int getBatchSize() {
                return userRoleList.size();
            }
        });
    }
}
