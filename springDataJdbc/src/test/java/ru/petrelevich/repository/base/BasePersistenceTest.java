package ru.petrelevich.repository.base;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(classes = TestPersistenceConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Testcontainers
public class BasePersistenceTest {

    @Container
    private static final TestContainersConfig.CustomPostgreSQLContainer
            postgreSQLContainer = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
}
