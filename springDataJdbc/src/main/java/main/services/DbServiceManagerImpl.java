package main.services;

import java.util.List;
import java.util.Optional;
import main.model.Manager;
import main.repository.ManagerRepository;
import main.transactions.TransactionRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DbServiceManagerImpl implements DBServiceManager {
    private static final Logger log = LoggerFactory.getLogger(DbServiceManagerImpl.class);

    private final ManagerRepository managerRepository;
    private final TransactionRunner transactionRunner;

    public DbServiceManagerImpl(ManagerRepository managerRepository, TransactionRunner transactionRunner) {
        this.managerRepository = managerRepository;
        this.transactionRunner = transactionRunner;
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(() -> {
            var savedManager = managerRepository.save(manager);

            log.info("savedManager manager: {}", savedManager);
            return savedManager;
        });
    }

    @Override
    public Optional<Manager> getManager(String no) {
        var managerOptional = managerRepository.findById(no);
        log.info("manager: {}", managerOptional);
        return managerOptional;
    }

    @Override
    public List<Manager> findAll() {
        var managerList = managerRepository.findAll();
        log.info("managerList:{}", managerList);
        return managerList;
    }
}
