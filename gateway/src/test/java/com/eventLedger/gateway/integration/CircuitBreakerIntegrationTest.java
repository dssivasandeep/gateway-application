package com.eventLedger.gateway.integration;


import com.eventLedger.gateway.client.AccountServiceClient;
import com.eventLedger.gateway.exception.AccountServiceUnavailableException;
import com.eventLedger.gateway.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CircuitBreakerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EventRepository eventRepository;

    @MockBean
    private AccountServiceClient accountServiceClient;

    @BeforeEach
    void setup() {

        eventRepository.deleteAll();

        Mockito.doThrow(
                        new AccountServiceUnavailableException(
                                "Account Service is unavailable"))
                .when(accountServiceClient)
                .applyTransaction(
                        anyString(),
                        any());
    }

    @Test
    void shouldReturn503WhenAccountServiceUnavailable()
            throws Exception {

        String payload =
                "{"
                        + "\"eventId\":\"evt-fail-001\","
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
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath(
                        "$.error")
                        .value("Account Service is unavailable"))
                .andExpect(status().isServiceUnavailable());
        org.junit.jupiter.api.Assertions.assertEquals(
                0,
                eventRepository.count());
    }
}
