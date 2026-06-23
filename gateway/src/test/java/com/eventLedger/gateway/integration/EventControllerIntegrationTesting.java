package com.eventLedger.gateway.integration;


import com.eventLedger.gateway.client.AccountServiceClient;
import com.eventLedger.gateway.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @BeforeEach
    void setup() {

        eventRepository.deleteAll();

        doNothing()
                .when(accountServiceClient)
                .applyTransaction(
                        org.mockito.ArgumentMatchers.anyString(),
                        org.mockito.ArgumentMatchers.any());
    }

    @Test
    void shouldCreateEventSuccessfully()
            throws Exception {

        String payload =
                "{"
                        + "\"eventId\":\"evt-001\","
                        + "\"accountId\":\"acct-123\","
                        + "\"type\":\"CREDIT\","
                        + "\"amount\":150,"
                        + "\"currency\":\"USD\","
                        + "\"eventTimestamp\":\"2026-05-15T14:02:11Z\""
                        + "}";

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("CREATED")));
    }

    @Test
    void shouldReturnDuplicateWhenEventExists()
            throws Exception {

        String payload =
                "{"
                        + "\"eventId\":\"evt-100\","
                        + "\"accountId\":\"acct-123\","
                        + "\"type\":\"CREDIT\","
                        + "\"amount\":100,"
                        + "\"currency\":\"USD\","
                        + "\"eventTimestamp\":\"2026-05-15T14:02:11Z\""
                        + "}";

        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload));

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath(
                        "$.status",
                        is("DUPLICATE")));
    }

    @Test
    void shouldRejectInvalidRequest()
            throws Exception {

        String payload =
                "{"
                        + "\"eventId\":\"evt-101\""
                        + "}";

        mockMvc.perform(
                        post("/events")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isBadRequest());
    }
}