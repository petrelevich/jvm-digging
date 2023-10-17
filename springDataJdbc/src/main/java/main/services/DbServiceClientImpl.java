package main.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import main.model.Client;
import main.repository.ClientRepository;
import main.transactions.TransactionRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final TransactionRunner transactionRunner;
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(TransactionRunner transactionRunner, ClientRepository clientRepository) {
        this.transactionRunner = transactionRunner;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(() -> {
            var savedClient = clientRepository.save(client);
            log.info("saved client: {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        var clientOptional = clientRepository.findById(id);
        log.info("client: {}", clientOptional);
        return clientOptional;
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<>(clientRepository.findAll());
        log.info("clientList:{}", clientList);
        return clientList;
    }
}
