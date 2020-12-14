package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.SomeObject;

@Repository
public interface SomeObjectRepository extends CrudRepository<SomeObject, Long> {
}
