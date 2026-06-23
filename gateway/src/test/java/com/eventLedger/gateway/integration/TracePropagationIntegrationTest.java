package com.eventLedger.gateway.integration;


import com.eventLedger.gateway.client.AccountServiceClient;
import com.eventLedger.gateway.dto.AccountTransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TracePropagationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AccountServiceClient accountServiceClient;

    @BeforeEach
    void setup() {

        doNothing()
                .when(accountServiceClient)
                .applyTransaction(
                        anyString(),
                        any(AccountTransactionRequest.class));
    }

    @Test
    void shouldPropagateTraceId()
            throws Exception {

        String payload =
                "{"
                        + "\"eventId\":\"evt-trace-001\","
                        + "\"accountId\":\"acct-123\","
                        + "\"type\":\"CREDIT\","
                        + "\"amount\":150,"
                        + "\"currency\":\"USD\","
                        + "\"eventTimestamp\":\"2026-05-15T14:02:11Z\""
                        + "}";

        mockMvc.perform(
                        post("/events")
                                .header(
                                        "X-Trace-Id",
                                        "TRACE-123")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(payload))
                .andExpect(status().isOk());

        verify(accountServiceClient)
                .applyTransaction(
                        anyString(),
                        any(AccountTransactionRequest.class));
    }
}
