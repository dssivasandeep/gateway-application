package com.eventLedger.gateway.dto;

import com.eventLedger.gateway.entity.EventType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@Data
public class EventRequest {

    @NotBlank
    private String eventId;

    @NotBlank
    private String accountId;

    @NotNull
    private EventType type;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotNull
    private Instant eventTimestamp;

    private Map<String, Object> metadata;
}
