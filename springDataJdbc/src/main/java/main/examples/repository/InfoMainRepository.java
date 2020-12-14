package main.examples.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import main.examples.model.InfoMain;

@Repository
public interface InfoMainRepository extends CrudRepository<InfoMain, Long> {
}
