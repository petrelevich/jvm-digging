package ru.usermng.dao;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.usermng.model.User;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoImplTest {
    private static final PostgreSQLContainer<PostgreSQLContainerShared> POSTGRE_SQL_CONTAINER = PostgreSQLContainerShared.getInstance();

    static {
        POSTGRE_SQL_CONTAINER.start();
    }

    @Test
    @DisplayName("Check user create and select functionality")
    public void userTest() throws SQLException, InterruptedException {

        //given
        var expectedUser = new User(1, "Vasil");
        UserDao userDao = new UserDaoImpl();
        var ds = new DataSource(POSTGRE_SQL_CONTAINER.getJdbcUrl(),
                POSTGRE_SQL_CONTAINER.getUsername(),
                POSTGRE_SQL_CONTAINER.getPassword());

        try (var connection = ds.getConnection()) {
            //when
            var userCreated = userDao.create(connection, expectedUser.getName());
            var userSelected = userDao.select(connection, expectedUser.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            //then
            assertThat(userCreated).isEqualTo(expectedUser);
            assertThat(userSelected).isEqualTo(expectedUser);
        }
    }
}