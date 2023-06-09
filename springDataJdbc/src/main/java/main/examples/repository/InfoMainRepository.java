package main.examples.repository;

import main.examples.model.InfoMain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoMainRepository extends CrudRepository<InfoMain, Long> {}
