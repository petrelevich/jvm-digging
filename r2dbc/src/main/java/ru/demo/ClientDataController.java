package ru.demo;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Readable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.demo.model.CreationResult;
import ru.demo.common.TimeComponent;
import ru.demo.model.Expenses;
import ru.demo.model.ExpensesValue;


//  http://localhost:8080/data/
@RestController
public class ClientDataController {
    private static final Logger log = LoggerFactory.getLogger(ClientDataController.class);

    private final TimeComponent timeComponent;

    private final ConnectionPool connectionPool;

    public ClientDataController(TimeComponent timeComponent, ConnectionPool connectionPool) {
        this.timeComponent = timeComponent;
        this.connectionPool = connectionPool;
    }

    @GetMapping(value = "/expenses/{id}")
    public Mono<Expenses> expensesGet(@PathVariable("id") long id) {
        log.info("expenses, id:{}", id);
        var select = """
                select id, expenses_sum, expenses_comment, expenses_date, processed_at, created_at, created_day_at
                  from expenses
                 where id = $1
                """;

        return Mono.usingWhen(connectionPool.create(),
                connection ->
                        Mono.from(connection
                                        .createStatement(select)
                                        .bind("$1", id)
                                        .execute())
                                .flatMap(result -> Mono.from(result.map(this::makeExpenses))),
                Connection::close);
    }

    @GetMapping(value = "/expenses")
    public Flux<Expenses> expensesGetAll() {
        log.info("expenses");
        var select = """
                select id, expenses_sum, expenses_comment, expenses_date, processed_at, created_at, created_day_at
                  from expenses
                """;

        return Flux.usingWhen(connectionPool.create(),
                connection ->
                        Flux.from(connection
                                        .createStatement(select)
                                        .execute())
                                .flatMap(result -> result.map(this::makeExpenses)),
                Connection::close);
    }


    @GetMapping(value = "/expenses-value/{id}")
    public Flux<ExpensesValue> expensesValueGet(@PathVariable("id") long id) {
        log.info("expenses, id:{}", id);
        var select = """
                select ex.*, eh.value_txt
                  from expenses ex
                 inner join expenses_history eh
                    on ex.id = eh.expenses_id
                 where ex.id = $1
                """;

        return Flux.usingWhen(connectionPool.create(),
                connection ->
                        Flux.from(connection
                                        .createStatement(select)
                                        .bind("$1", id)
                                        .execute())
                                .flatMap(result -> result.map(this::makeExpensesValue)),
                Connection::close);
    }


    @PostMapping(value = "/expenses")
    public Mono<CreationResult> expensesCreate(@RequestBody Expenses expenses) {
        log.info("expenses:{}", expenses);
        var now = timeComponent.now();
        var insert = """
                insert into expenses(expenses_sum, expenses_comment, expenses_date, created_at, created_day_at)
                     values ($1, $2, $3, $4, $5)
                """;
        var commit = true;
        return Mono.usingWhen(connectionPool.create(),
                connection ->
                        Mono.from(connection.beginTransaction())
                                .then(Mono.from(connection
                                                .createStatement(insert)
                                                .bind("$1", expenses.expensesSum())
                                                .bind("$2", expenses.expensesComment())
                                                .bind("$3", expenses.expensesDate())
                                                .bind("$4", now)
                                                .bind("$5", now.toLocalDate())
                                                .returnGeneratedValues("id")
                                                .execute())
                                        .flatMap(result ->
                                                commit ?
                                                        Mono.from(connection.commitTransaction())
                                                                .then(Mono.from(result.map(this::makeCreationResult)))
                                                        :
                                                        Mono.from(connection.rollbackTransaction())
                                                                .then(Mono.just(new CreationResult(null, "rollback")))

                                        )
                                )
                , Connection::close);
    }


    private CreationResult makeCreationResult(Readable row) {
        return new CreationResult(row.get("Id", Long.class), null);
    }

    private Expenses makeExpenses(Readable row) {
        return new Expenses(
                row.get("Id", Long.class),
                row.get("expenses_sum", BigDecimal.class),
                row.get("expenses_comment", String.class),
                row.get("expenses_date", LocalDate.class),

                row.get("processed_at", LocalDateTime.class),
                row.get("created_at", LocalDateTime.class),
                row.get("created_day_at", LocalDate.class)
        );
    }

    private ExpensesValue makeExpensesValue(Readable row) {
        return new ExpensesValue(
                row.get("Id", Long.class),
                row.get("expenses_sum", BigDecimal.class),
                row.get("expenses_comment", String.class),
                row.get("expenses_date", LocalDate.class),

                row.get("processed_at", LocalDateTime.class),
                row.get("created_at", LocalDateTime.class),
                row.get("created_day_at", LocalDate.class),
                row.get("value_txt", String.class)
        );
    }
}
