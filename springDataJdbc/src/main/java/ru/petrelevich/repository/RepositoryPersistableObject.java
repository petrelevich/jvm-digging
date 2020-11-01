package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.PersistableObject;

@Repository
public interface RepositoryPersistableObject extends CrudRepository<PersistableObject, Long> {
}
