package ru.usermng.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.usermng.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public User create(Connection connection, String name) {
        try {
            try (var pst = connection.prepareStatement("insert into users(name, nick_name) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, name);
                pst.setString(2, name);
                pst.executeUpdate();
                try (var keys = pst.getGeneratedKeys()) {
                    keys.next();
                    var user = new User(keys.getLong(1), name);
                    logger.info("created user:{}", user);
                    return user;
                }
            }
        } catch (SQLException ex) {
            throw new UserOperationException("User creation error", ex);
        }
    }

    @Override
    public Optional<User> select(Connection connection, long id) {
        try {
            try (var pst = connection.prepareStatement("select coalesce(nick_name, name) as nick_name " +
                    "from users where id  = ?")) {
                pst.setLong(1, id);
                try (var rs = pst.executeQuery()) {
                    if (rs.next()) {
                        var user = new User(id, rs.getString("nick_name"));
                        logger.info("selected name:{}", user);
                        return Optional.of(user);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new UserOperationException("User selection error", ex);
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectAll(Connection connection) {
        var userList = new ArrayList<User>();
        try {
            try (var pst = connection.prepareStatement("select id, coalesce(nick_name, name) as nick_name from users")) {
                try (var rs = pst.executeQuery()) {
                    while (rs.next()) {
                        userList.add(new User(rs.getLong("id"), rs.getString("nick_name")));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new UserOperationException("User selection error", ex);
        }
        return userList;
    }
}
