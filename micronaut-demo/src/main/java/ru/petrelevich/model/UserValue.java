package ru.petrelevich.model;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity
public class UserValue {

    @GeneratedValue
    @Id
    private Long id;
    private final String usrValue;

    public UserValue(String usrValue) {
        this.usrValue = usrValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsrValue() {
        return usrValue;
    }

    @Override
    public String toString() {
        return "UserValue{" +
                "id=" + id +
                ", usrValue='" + usrValue + '\'' +
                '}';
    }
}
