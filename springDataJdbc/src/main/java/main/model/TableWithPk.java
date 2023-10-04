package main.model;

import static org.springframework.data.relational.core.mapping.Embedded.OnEmpty.USE_EMPTY;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;

@Table("table_with_pk")
public class TableWithPk implements Persistable<TableWithPk.Pk> {

    @Id
    @Embedded(onEmpty = USE_EMPTY)
    private final Pk pk;

    private final String value;

    @Transient
    private final boolean isNew;

    @PersistenceCreator
    public TableWithPk(Pk pk, String value) {
        this(pk, value, false);
    }

    public TableWithPk(Pk pk, String value, boolean isNew) {
        this.pk = pk;
        this.value = value;
        this.isNew = isNew;
    }

    @Override
    public Pk getId() {
        return pk;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public Pk getPk() {
        return getId();
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TableWithPk{" + "pk=" + pk + ", value='" + value + '\'' + ", isNew=" + isNew + '}';
    }

    public record Pk(String idPart1, String idPart2) {}
}
