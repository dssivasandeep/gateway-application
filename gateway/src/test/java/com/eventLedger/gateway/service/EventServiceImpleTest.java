package com.eventLedger.gateway.service;


import com.eventLedger.gateway.client.AccountServiceClient;
import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.entity.EventEntity;
import com.eventLedger.gateway.entity.EventType;
import com.eventLedger.gateway.metrics.MetricsService;
import com.eventLedger.gateway.repository.EventRepository;
import com.eventLedger.gateway.serviceImpl.EventServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private AccountServiceClient accountServiceClient;

    @Mock
    private MetricsService metricsService;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        eventService =
                new EventServiceImpl(
                        eventRepository,
                        new ObjectMapper(),
                        accountServiceClient,
                        metricsService
                );
    }

    @Test
    void shouldRejectDuplicateEvent() {

        EventEntity existing =
                EventEntity.builder()
                        .eventId("evt-001")
                        .build();

        when(eventRepository.findByEventId("evt-001"))
                .thenReturn(Optional.of(existing));

        EventRequest request = new EventRequest();
        request.setEventId("evt-001");

        eventService.submitEvent(request);

        verify(metricsService).incrementDuplicate();

        verify(eventRepository, never())
                .save(any());
    }

    @Test
    void shouldSaveNewEvent() {

        when(eventRepository.findByEventId("evt-002"))
                .thenReturn(Optional.empty());

        EventRequest request = new EventRequest();

        request.setEventId("evt-002");
        request.setAccountId("acct-123");
        request.setType(EventType.CREDIT);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("USD");
        request.setEventTimestamp(Instant.now());

        eventService.submitEvent(request);

        verify(eventRepository)
                .save(any(EventEntity.class));

        verify(metricsService)
                .incrementSubmitted();
    }
}
