package main.examples.repository;

import main.examples.model.InfoAdditional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoAdditionalRepository extends CrudRepository<InfoAdditional, Long> {}
