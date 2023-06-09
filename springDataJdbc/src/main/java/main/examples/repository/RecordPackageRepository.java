package main.examples.repository;

import main.examples.model.RecordPackage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordPackageRepository extends CrudRepository<RecordPackage, Long> {}
