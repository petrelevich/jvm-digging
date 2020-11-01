package ru.petrelevich.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("persistable_object")
public class PersistableObject implements Persistable<Long> {

    @Id
    private final Long id;
    private final String name;
    private final String data;

    public PersistableObject(Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
