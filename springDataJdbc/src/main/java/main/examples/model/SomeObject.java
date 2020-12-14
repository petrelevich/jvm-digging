package main.examples.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("some_object")
public class SomeObject {
    @Id
    private final Long id;
    private final String name;
    private final String data;

    public SomeObject(Long id, String name, String data) {
        this.id = id;
        this.name = name;
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
