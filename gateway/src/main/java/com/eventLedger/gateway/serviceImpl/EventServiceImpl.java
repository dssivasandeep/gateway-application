package com.eventLedger.gateway.serviceImpl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.dto.EventResponse;
import com.eventLedger.gateway.entity.EventEntity;
import com.eventLedger.gateway.repository.EventRepository;
import com.eventLedger.gateway.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @Override
    public EventResponse submitEvent(EventRequest request) {

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

        EventEntity event = EventEntity.builder()
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
