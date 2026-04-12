package main.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@Table("client")
public record Client(
    @Id @Column("id") Long id,
    @Nonnull String name,
    String managerId,
    Integer orderColumn,
    @MappedCollection(idColumn = "client_id") ClientDetails clientDetails) {}
