package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import main.model.Client;
import main.model.ClientDetails;
import main.model.Manager;
import main.model.TableWithPk;
import main.repository.ClientRepository;
import main.repository.ManagerRepository;
import main.repository.TableWithPkRepository;
import main.services.DBServiceClient;
import main.services.DBServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component("actionDemo")
public class ActionDemo implements CommandLineRunner {
  private static final Logger log = LoggerFactory.getLogger(ActionDemo.class);

  private final ClientRepository clientRepository;
  private final ManagerRepository managerRepository;
  private final DBServiceClient dbServiceClient;
  private final DBServiceManager dbServiceManager;

  private final TableWithPkRepository tableWithPkRepository;

  public ActionDemo(
      ClientRepository clientRepository,
      ManagerRepository managerRepository,
      DBServiceClient dbServiceClient,
      DBServiceManager dbServiceManager,
      TableWithPkRepository tableWithPkRepository) {
    this.managerRepository = managerRepository;
    this.clientRepository = clientRepository;
    this.dbServiceClient = dbServiceClient;
    this.dbServiceManager = dbServiceManager;
    this.tableWithPkRepository = tableWithPkRepository;
  }

  @Override
  public void run(String... args) {

    // создаем Manager
    log.info(">>> manager creation");
    dbServiceManager.saveManager(
        new Manager(
            "m:" + System.currentTimeMillis(),
            "ManagerFirst",
            new HashSet<>(),
            new ArrayList<>(),
            true));

    var managerName = "m:" + System.currentTimeMillis();
    var managerSecond =
        dbServiceManager.saveManager(
            new Manager(
                managerName,
                "ManagerSecond",
                Set.of(
                    new Client(
                        null, "managClient1", managerName, 11, new ClientDetails(null, "inf01")),
                    new Client(
                        null, "managClient2", managerName, 22, new ClientDetails(null, "info2"))),
                List.of(
                    new Client(
                        null,
                        "managClient1Ordered",
                        managerName,
                        100,
                        new ClientDetails(null, "inf01")),
                    new Client(
                        null,
                        "managClient2Ordered",
                        managerName,
                        200,
                        new ClientDetails(null, "info2"))),
                true));
    log.info(">>> get Manager:{}", managerSecond.getId());
    var managerSecondSelected =
        dbServiceManager
            .getManager(managerSecond.getId())
            .orElseThrow(
                () -> new RuntimeException("Manager not found, id:" + managerSecond.getId()));
    log.info(">>> managerSecondSelected:{}", managerSecondSelected);

    // обновляем Manager с удалением его клиентов
    dbServiceManager.saveManager(
        new Manager(
            managerSecondSelected.getId(),
            "dbServiceSecondUpdated",
            new HashSet<>(),
            new ArrayList<>(),
            false));
    var managerUpdated =
        dbServiceManager
            .getManager(managerSecondSelected.getId())
            .orElseThrow(
                () ->
                    new RuntimeException("Manager not found, id:" + managerSecondSelected.getId()));
    log.info(">>> managerUpdated:{}", managerUpdated);

    // создаем Client
    var firstClient =
        dbServiceClient.saveClient(
            new Client(
                null,
                "dbServiceFirst" + System.currentTimeMillis(),
                managerSecond.getId(),
                1,
                new ClientDetails(null, "init1")));

    var clientSecond =
        dbServiceClient.saveClient(
            new Client(
                null,
                "dbServiceSecond" + System.currentTimeMillis(),
                managerSecond.getId(),
                2,
                new ClientDetails(null, "init2")));
    var clientSecondSelected =
        dbServiceClient
            .getClient(clientSecond.id())
            .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.id()));
    log.info(">>> clientSecondSelected:{}", clientSecondSelected);

    log.info("delete instead of update");
    // обновляем Client
    dbServiceClient.saveClient(
        new Client(
            clientSecondSelected.id(),
            "dbServiceSecondUpdated",
            managerSecond.getId(),
            clientSecondSelected.orderColumn(),
            new ClientDetails(null, "updated")));
    var clientUpdated =
        dbServiceClient
            .getClient(clientSecondSelected.id())
            .orElseThrow(
                () -> new RuntimeException("Client not found, id:" + clientSecondSelected.id()));
    log.info("clientUpdated:{}", clientUpdated);

    // получаем все сущности
    log.info(">>> All clients");
    dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

    log.info(">>> All managers");
    dbServiceManager.findAll().forEach(manager -> log.info("manager:{}", manager));

    // применяем переопределенные методы репозитариев
    var clientFoundByName =
        clientRepository
            .findByName(firstClient.name())
            .orElseThrow(
                () -> new RuntimeException("client not found, name:" + firstClient.name()));
    log.info("clientFoundByName:{}", clientFoundByName);

    var clientFoundByNameIgnoreCase =
        clientRepository
            .findByNameIgnoreCase(firstClient.name().toLowerCase())
            .orElseThrow(
                () -> new RuntimeException("client not found, name:" + firstClient.name()));
    log.info("clientFoundByNameIgnoreCase:{}", clientFoundByNameIgnoreCase);

    clientRepository.updateName(firstClient.id(), "newName");
    var updatedClient =
        clientRepository
            .findById(firstClient.id())
            .orElseThrow(() -> new RuntimeException("client not found"));

    log.info(">>> updatedClient:{}", updatedClient);

    // проверяем проблему N+1
    log.info(">>> checking N+1 problem");
    var managerId = managerSecond.getId();

    var managerN =
        managerRepository
            .findById(managerId)
            .orElseThrow(
                () -> new RuntimeException("Manager not found, name:" + managerSecond.getId()));
    log.info(">>> managerN:{}", managerN);

    /*
    получаем основную сущность:
    [SELECT "manager"."id" AS "id", "manager"."label" AS "label" FROM "manager" WHERE "manager"."id" = ?]
    получаем дочерние:
    [SELECT "client"."id" AS "id", "client"."name" AS "name", "client"."manager_id" AS "manager_id" FROM "client" WHERE "client"."manager_id" = ?]
    */
    log.info(">>> select all");
    var allManagers = managerRepository.findAll();
    log.info(">>> allManagers.size():{}", allManagers.size());
    log.info(">>> allManagers:{}", allManagers);

    //
    log.info(">>> TableWithPkRepository");
    var pk = new TableWithPk.Pk("p1", String.valueOf(System.currentTimeMillis()));
    TableWithPk tableWithPk = new TableWithPk(pk, "value", true);
    log.info("tableWithPk:{}", tableWithPk);

    tableWithPkRepository.saveEntry(tableWithPk);

    TableWithPk loadedTableWithPk = tableWithPkRepository.findById(pk).orElseThrow();
    log.info("loadedTableWithPk:{}", loadedTableWithPk);
  }
}
