package main.demo.repository;

import java.util.List;
import java.util.Optional;
import main.demo.model.Dog;
import main.demo.model.GuardDog;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends CrudRepository<Dog, Long> {

    @Query("select * from dog where name = :name")
    List<Dog> findByName(@Param("name") String dogName);

    @Query(value = "select * from dog where name = :name", rowMapperClass = GuardDogMapper.class)
    Optional<GuardDog> findGuardDogByName(@Param("name") String name);
}
