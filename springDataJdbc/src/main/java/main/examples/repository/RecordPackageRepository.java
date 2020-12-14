package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.RecordPackage;

@Repository
public interface RecordPackageRepository extends CrudRepository<RecordPackage, Long> {
}
