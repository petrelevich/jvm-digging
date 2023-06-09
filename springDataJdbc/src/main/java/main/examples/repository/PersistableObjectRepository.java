package main.examples.repository;

import main.examples.model.PersistableObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistableObjectRepository extends CrudRepository<PersistableObject, Long> {}
