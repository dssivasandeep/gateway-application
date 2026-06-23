package com.eventLedger.gateway.mapper;


import com.eventLedger.gateway.dto.EventResponse;
import com.eventLedger.gateway.entity.EventEntity;

public class EventMapper {

    private EventMapper() {
    }

    public static EventResponse toResponse(EventEntity entity) {

        return EventResponse.builder()
                .eventId(entity.getEventId())
                .accountId(entity.getAccountId())
                .type(entity.getType())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .eventTimestamp(entity.getEventTimestamp())
                .status("SUCCESS")
                .build();
    }
}
