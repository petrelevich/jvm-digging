package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.Record;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {
}
