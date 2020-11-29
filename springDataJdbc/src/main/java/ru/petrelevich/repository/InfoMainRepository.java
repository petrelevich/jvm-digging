package ru.petrelevich.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.petrelevich.model.InfoMain;

@Repository
public interface InfoMainRepository extends CrudRepository<InfoMain, Long> {
}
