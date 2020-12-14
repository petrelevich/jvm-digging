package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.PersistableObject;

@Repository
public interface PersistableObjectRepository extends CrudRepository<PersistableObject, Long> {
}
