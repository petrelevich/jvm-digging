package com.datasrc.deduplicator;

import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

import com.datasrc.model.StringValue;
import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.postgresql.util.PSQLException;

public class DeDuplicatorDb implements DeDuplicator {
    private final DataSource dataSource;

    public DeDuplicatorDb(DataSource dataSource) {
        this.dataSource = dataSource;
        flywayMigrations(dataSource);
    }

    @Override
    public boolean process(StringValue value, StringValueProcessor processor) {
        try {
            try (var connection = dataSource.getConnection();
                    var pst =
                            connection.prepareStatement("insert into kafka_processed_value_ids(value_id) values (?)")) {
                pst.setLong(1, value.id());
                pst.executeUpdate();
                processor.accept(value);
                connection.commit();
            }
        } catch (Exception ex) {
            if (ex instanceof PSQLException exPsql
                    && (UNIQUE_VIOLATION.getState().equals(exPsql.getSQLState()))) {
                return false;
            }
            throw new DeDuplicatorException("process value:" + value + "error:" + ex.getMessage(), ex);
        }
        return true;
    }

    private void flywayMigrations(DataSource dataSource) {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }
}
