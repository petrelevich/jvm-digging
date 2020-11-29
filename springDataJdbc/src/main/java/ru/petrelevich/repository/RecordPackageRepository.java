package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.RecordPackage;

@Repository
public interface RecordPackageRepository extends CrudRepository<RecordPackage, Long> {
}
