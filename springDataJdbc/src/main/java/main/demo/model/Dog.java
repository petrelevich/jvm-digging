package main.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("dog")
public class Dog {

    @Id
    private final Long dogId;
    private final String name;
    private final String ownerName;

    public Dog(String name) {
        this(null, name, null);
    }

    public Dog(String name, String ownerName) {
        this(null, name, ownerName);
    }

    @PersistenceConstructor
    public Dog(Long dogId, String name, String ownerName) {
        this.dogId = dogId;
        this.name = name;
        this.ownerName = ownerName;
    }

    public Long getDogId() {
        return dogId;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogId=" + dogId +
                ", name='" + name + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}
