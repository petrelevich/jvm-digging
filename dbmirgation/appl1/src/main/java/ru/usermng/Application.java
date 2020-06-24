package ru.usermng;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.usermng.dao.DataSource;
import ru.usermng.dao.UserDaoImpl;

import java.sql.SQLException;


public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/usersDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";


    public static void main(String[] args) throws SQLException {
        flywayMigrations(URL, USER, PASSWORD);
        new Application().run();
    }

    public static void flywayMigrations(String url, String user, String password) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(url, user, password)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
    }

    private void run() throws SQLException {
        var ds = new DataSource(URL, USER, PASSWORD);
        var userDao = new UserDaoImpl();

        try(var connection = ds.getConnection()) {
            var userCreated = userDao.create(connection, "ivanko BXd");
            logger.info("user created:{}", userCreated);

            var userSelected = userDao.select(connection, userCreated.getId());
            logger.info("user selected:{}", userSelected);

            logger.info("All users");
            var userList = userDao.selectAll(connection);
            userList.forEach(user -> logger.info("user:{}", user));
        }
    }
}
