package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.Record;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {
}
