package com.eventLedger.gateway.dto;


import com.eventLedger.gateway.entity.EventType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class EventResponse {

    private String eventId;

    private String accountId;

    private EventType type;

    private BigDecimal amount;

    private String currency;

    private Instant eventTimestamp;

    private String status;
}
