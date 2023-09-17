package main.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("client_details")
public class ClientDetails {
    @Id
    private final Long clientId;

    @Nonnull
    private final String info;

    public ClientDetails(String info) {
        this(null, info);
    }

    @PersistenceCreator
    public ClientDetails(Long clientId, String info) {
        this.clientId = clientId;
        this.info = info;
    }

    public Long getClientId() {
        return clientId;
    }

    @Nonnull
    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "ClientDetails{" +
                "clientId=" + clientId +
                ", info='" + info + '\'' +
                '}';
    }
}
