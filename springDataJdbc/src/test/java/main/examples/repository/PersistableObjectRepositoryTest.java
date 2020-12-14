package main.examples.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import main.examples.model.PersistableObject;
import main.examples.repository.base.BasePersistenceTest;

import static org.assertj.core.api.Assertions.assertThat;

class PersistableObjectRepositoryTest extends BasePersistenceTest {

    @Autowired
    PersistableObjectRepository repository;

    @Test
    void saveTestWithNullId() {
        var object = new PersistableObject(true, null, "name", "value");

        var savedObject = repository.save(object);
        assertThat(savedObject).isNotNull();
        assertThat(savedObject.getId()).isNotNull();

        var selectedObject = repository.findById(savedObject.getId());
        assertThat(selectedObject).isNotNull();
    }

    @Test
    void saveTestWithNotNullId() {
        var objectId = 10L;
        var object = new PersistableObject(true, objectId, "name", "value");

        var savedObject = repository.save(object);
        assertThat(savedObject).isNotNull();
        assertThat(savedObject.getId()).isNotNull();
        assertThat(savedObject.getId()).isEqualTo(objectId);

        var selectedObject = repository.findById(savedObject.getId());
        assertThat(selectedObject).isNotNull();
    }
}