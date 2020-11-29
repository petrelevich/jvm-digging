package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.SomeObject;

@Repository
public interface SomeObjectRepository extends CrudRepository<SomeObject, Long> {
}
