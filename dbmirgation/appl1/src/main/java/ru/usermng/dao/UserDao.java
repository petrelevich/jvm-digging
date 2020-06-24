package ru.usermng.dao;

import ru.usermng.model.User;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User create(Connection connection, String name);

    Optional<User> select(Connection connection, long id);

    List<User> selectAll(Connection connection);
}
