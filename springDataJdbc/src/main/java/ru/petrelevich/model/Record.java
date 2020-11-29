package ru.petrelevich.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Table("record")
public class Record {
    @Id
    private final Long recordId;
    private final Long recordPackageId;
    private final String data;

    public Record(String data) {
        this.recordId = null;
        this.recordPackageId = null;
        this.data = data;
    }

    @PersistenceConstructor
    private Record(Long recordId, Long recordPackageId, String data) {
        this.recordId = recordId;
        this.recordPackageId = recordPackageId;
        this.data = data;
    }

    public Long getRecordId() {
        return recordId;
    }

    public Long getRecordPackageId() {
        return recordPackageId;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", recordPackageId=" + recordPackageId +
                ", data='" + data + '\'' +
                '}';
    }
}
