package main.services;

import java.util.List;
import java.util.Optional;
import main.model.Manager;

public interface DBServiceManager {

    Manager saveManager(Manager client);

    Optional<Manager> getManager(String no);

    List<Manager> findAll();
}
