package ru.demo.mainpackage.model;

import jakarta.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

@Table("payments")
@Builder(toBuilder = true)
public class Payment {

    @Id
    private final Long id;

    @Nonnull
    private final String cardAccount;

    @Nonnull
    private final BigDecimal amount;

    @Nonnull
    private final PaymentStatus status;

    @Nonnull
    private final LocalDateTime createdAt;

    @Nonnull
    private final LocalDateTime updatedAt;

    @PersistenceCreator
    private Payment(
            Long id,
            String cardAccount,
            BigDecimal amount,
            PaymentStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.cardAccount = cardAccount;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getCardAccount() {
        return cardAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Payment{" + "id="
                + id + ", cardAccount='"
                + cardAccount + '\'' + ", amount="
                + amount + ", status="
                + status + ", createdAt="
                + createdAt + ", updatedAt="
                + updatedAt + '}';
    }
}
