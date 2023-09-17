package main.model;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("manager")
public class Manager implements Persistable<String> {

    @Id
    @Nonnull
    private final String id;
    private final String label;

    @MappedCollection(idColumn = "manager_id")
    private final Set<Client> clients;

    @MappedCollection(idColumn = "manager_id", keyColumn= "order_column")
    private final List<Client> clientsOrdered;

    @Transient
    private final boolean isNew;

    public Manager(String id, String label, Set<Client> clients, List<Client> clientsOrdered, boolean isNew) {
        this.id = id;
        this.label = label;
        this.clients = clients;
        this.clientsOrdered = clientsOrdered;
        this.isNew = isNew;
    }

    @PersistenceCreator
    private Manager(String id, String label, Set<Client> clients, List<Client> clientsOrdered) {
        this(id, label, clients, clientsOrdered, false);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public Set<Client> getClients() {
        return clients;
    }

    public List<Client> getClientsOrdered() {
        return clientsOrdered;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", clients=" + clients +
                ", clientsOrdered=" + clientsOrdered +
                ", isNew=" + isNew +
                '}';
    }
}
