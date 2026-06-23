package com.eventLedger.gateway.dto;


import com.eventLedger.gateway.entity.EventType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class AccountTransactionRequest {

    private String eventId;

    private EventType type;

    private BigDecimal amount;

    private Instant eventTimestamp;
}