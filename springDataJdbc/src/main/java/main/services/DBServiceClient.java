package main.services;

import java.util.List;
import java.util.Optional;
import main.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
