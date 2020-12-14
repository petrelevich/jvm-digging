package main.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Collection;
import java.util.Set;

@Table("owner")
public class Owner implements Persistable<String> {

    @Id
    private final String ownerName;
    private final String address;

    @MappedCollection(idColumn = "owner_name")
    private final Set<Dog> dogs;

    @Transient
    private final boolean isNew;

    public Owner(String ownerName, String address, Set<Dog> dogs, boolean isNew) {
        this.ownerName = ownerName;
        this.address = address;
        this.isNew = isNew;
        this.dogs = dogs;
    }

    @PersistenceConstructor
    public Owner(String ownerName, String address, Set<Dog> dogs) {
        this(ownerName, address, dogs, false);
    }

    @Override
    public String getId() {
        return ownerName;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public String getAddress() {
        return address;
    }

    public Set<Dog> getDogs() {
        return dogs;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "ownerName='" + ownerName + '\'' +
                ", address='" + address + '\'' +
                ", isNew=" + isNew +
                ", dogs=" + dogs +
                '}';
    }
}
