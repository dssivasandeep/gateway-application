package com.eventLedger.gateway.controller;


import com.eventLedger.gateway.dto.EventRequest;
import com.eventLedger.gateway.dto.EventResponse;
import com.eventLedger.gateway.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping
    public EventResponse submitEvent(
            @Valid @RequestBody EventRequest request) {

        return eventService.submitEvent(request);
    }

    @GetMapping("/{eventId}")
    public EventResponse getEvent(
            @PathVariable String eventId) {

        return eventService.getEvent(eventId);
    }

    @GetMapping
    public List<EventResponse> getEvents(
            @RequestParam String account) {

        return eventService.getEventsByAccount(account);
    }
}
