package com.datasrc;

import java.util.Objects;

public record StringValue(long id, String value) {

    public long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return id == that.id && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
