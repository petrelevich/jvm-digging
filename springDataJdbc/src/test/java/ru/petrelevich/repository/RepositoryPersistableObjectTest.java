package ru.petrelevich.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.petrelevich.model.PersistableObject;
import ru.petrelevich.repository.base.BasePersistenceTest;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryPersistableObjectTest extends BasePersistenceTest {

    @Autowired
    RepositoryPersistableObject repository;

    @Test
    void saveTestWithNullId() {
        var object = new PersistableObject(1L, "name", "value");

        var savedObject = repository.save(object);
        assertThat(savedObject).isNotNull();
        assertThat(savedObject.getId()).isNotNull();
    }

    @Test
    void saveTestWithNotNullId() {
        var objectId = 10L;
        var object = new PersistableObject(objectId, "name", "value");

        var savedObject = repository.save(object);
        assertThat(savedObject).isNotNull();
        assertThat(savedObject.getId()).isEqualTo(objectId);
    }
}