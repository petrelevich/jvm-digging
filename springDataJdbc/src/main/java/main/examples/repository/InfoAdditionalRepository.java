package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.InfoAdditional;

@Repository
public interface InfoAdditionalRepository extends CrudRepository<InfoAdditional, Long> {
}
