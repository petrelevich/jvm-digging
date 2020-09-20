package ru.trademgr.events;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import ru.trademgr.model.Side;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class CreatedTradeEvent {
    UUID eventId = UUID.randomUUID();
    long tradeId;

    String shortName;
    Side side;
    long size;
    BigDecimal price;

    @JsonCreator
    public CreatedTradeEvent(@JsonProperty("tradeId") long tradeId,
                 @JsonProperty("shortName") String shortName,
                 @JsonProperty("side") Side side,
                 @JsonProperty("size") long size,
                 @JsonProperty("price") BigDecimal price) {
        this.tradeId = tradeId;
        this.shortName = shortName;
        this.side = side;
        this.size = size;
        this.price = price;
    }
}
