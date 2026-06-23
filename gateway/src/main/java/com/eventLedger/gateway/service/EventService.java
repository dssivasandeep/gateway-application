package com.eventLedger.gateway.service;


import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.dto.EventResponse;

import java.util.List;

public interface EventService {

    EventResponse submitEvent(EventRequest request);

    EventResponse getEvent(String eventId);

    List<EventResponse> getEventsByAccount(String accountId);
}
