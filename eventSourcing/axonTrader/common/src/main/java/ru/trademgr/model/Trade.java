package ru.trademgr.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Value
@Builder
public class Trade {
    long tradeId;
    LocalDateTime tradeTime;
    LocalDate settlementDate;
    String name;
    String shortName;
    String currency;
    Side side;
    long size;
    BigDecimal price;
    BigDecimal amount;
    BigDecimal accruedInterest;
    BigDecimal brokerFee;
    BigDecimal marketFee;

    @JsonCreator
    public Trade(@JsonProperty("tradeId") long tradeId,
                 @JsonProperty("tradeTime") LocalDateTime tradeTime,
                 @JsonProperty("settlementDate") LocalDate settlementDate,
                 @JsonProperty("name") String name,
                 @JsonProperty("shortName") String shortName,
                 @JsonProperty("currency") String currency,
                 @JsonProperty("side") Side side,
                 @JsonProperty("size") long size,
                 @JsonProperty("price") BigDecimal price,
                 @JsonProperty("amount") BigDecimal amount,
                 @JsonProperty("accruedInterest") BigDecimal accruedInterest,
                 @JsonProperty("brokerFee") BigDecimal brokerFee,
                 @JsonProperty("marketFee") BigDecimal marketFee) {
        this.tradeId = tradeId;
        this.tradeTime = tradeTime;
        this.settlementDate = settlementDate;
        this.name = name;
        this.shortName = shortName;
        this.currency = currency;
        this.side = side;
        this.size = size;
        this.price = price;
        this.amount = amount;
        this.accruedInterest = accruedInterest;
        this.brokerFee = brokerFee;
        this.marketFee = marketFee;
    }
}
