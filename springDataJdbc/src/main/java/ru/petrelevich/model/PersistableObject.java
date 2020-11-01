package ru.petrelevich.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table("persistable_object")
public class PersistableObject implements Persistable<Long> {

    @Id
    private final Long id;
    private final String name;
    private final String data;

    @Transient
    private final boolean isNew;

    @PersistenceConstructor
    private PersistableObject(Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.isNew = false;
    }

    public PersistableObject(boolean isNew, Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.isNew = isNew;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
