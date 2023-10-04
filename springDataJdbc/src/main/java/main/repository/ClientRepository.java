package main.repository;

import java.util.Optional;
import main.model.Client;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends ListCrudRepository<Client, Long> {

    Optional<Client> findByName(String name);

    @Query("select * from client where upper(name) = upper(:name)")
    Optional<Client> findByNameIgnoreCase(@Param("name") String name);

    @Modifying
    @Query("update client set name = :newName where id = :id")
    void updateName(@Param("id") long id, @Param("newName") String newName);
}
