package main.examples.repository;

import main.examples.model.SomeObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SomeObjectRepository extends CrudRepository<SomeObject, Long> {}
