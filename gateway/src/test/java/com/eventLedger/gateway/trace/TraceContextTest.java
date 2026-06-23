package com.eventLedger.gateway.trace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TraceContextTest {

    @Test
    void shouldStoreAndRetrieveTraceId() {

        TraceContext.setTraceId("TRACE-123");

        assertEquals(
                "TRACE-123",
                TraceContext.getTraceId());

        TraceContext.clear();

        assertNull(
                TraceContext.getTraceId());
    }
}