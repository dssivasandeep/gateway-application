package com.eventLedger.gateway.service;


import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.dto.EventResponse;

public interface EventService {

    EventResponse submitEvent(EventRequest request);
}
