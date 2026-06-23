package com.eventLedger.gateway.serviceImpl;


import com.eventLedger.gateway.client.AccountServiceClient;
import com.eventLedger.gateway.dto.AccountTransactionRequest;
import com.eventLedger.gateway.trace.TraceContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.dto.EventResponse;
import com.eventLedger.gateway.entity.EventEntity;
import com.eventLedger.gateway.repository.EventRepository;
import com.eventLedger.gateway.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;
    private final AccountServiceClient accountServiceClient;

    @Override
    @Transactional
    public EventResponse submitEvent(EventRequest request) {

        log.info(
                "Processing event={} traceId={}",
                request.getEventId(),
                TraceContext.getTraceId()
        );
        EventEntity existing =
                eventRepository.findByEventId(request.getEventId())
                        .orElse(null);

        if (existing != null) {

            return EventResponse.builder()
                    .eventId(existing.getEventId())
                    .accountId(existing.getAccountId())
                    .type(existing.getType())
                    .amount(existing.getAmount())
                    .currency(existing.getCurrency())
                    .eventTimestamp(existing.getEventTimestamp())
                    .status("DUPLICATE")
                    .build();
        }

        AccountTransactionRequest transactionRequest =
                AccountTransactionRequest.builder()
                        .eventId(request.getEventId())
                        .type(request.getType())
                        .amount(request.getAmount())
                        .eventTimestamp(request.getEventTimestamp())
                        .build();

        accountServiceClient.applyTransaction(
                request.getAccountId(),
                transactionRequest
        );

        EventEntity event =
                EventEntity.builder()
                        .eventId(request.getEventId())
                        .accountId(request.getAccountId())
                        .type(request.getType())
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .eventTimestamp(request.getEventTimestamp())
                        .metadata(convertMetadata(request))
                        .build();

        eventRepository.save(event);

        return EventResponse.builder()
                .eventId(event.getEventId())
                .accountId(event.getAccountId())
                .type(event.getType())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .eventTimestamp(event.getEventTimestamp())
                .status("CREATED")
                .build();
    }

    private String convertMetadata(EventRequest request) {

        try {
            return objectMapper.writeValueAsString(
                    request.getMetadata());
        } catch (JsonProcessingException ex) {
            return "{}";
        }
    }
}
