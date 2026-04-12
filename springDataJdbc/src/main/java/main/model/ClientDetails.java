package main.model;

import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("client_details")
public record ClientDetails(@Id Long clientId, @Nonnull String info) {}
