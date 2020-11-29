package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.InfoAdditional;

@Repository
public interface InfoAdditionalRepository extends CrudRepository<InfoAdditional, Long> {
}
