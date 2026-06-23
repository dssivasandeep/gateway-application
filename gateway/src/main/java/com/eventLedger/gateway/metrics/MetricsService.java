package com.eventLedger.gateway.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class MetricsService {

    private final Counter submittedCounter;
    private final Counter duplicateCounter;
    private final Counter accountFailureCounter;

    public MetricsService(MeterRegistry meterRegistry) {

        this.submittedCounter =
                meterRegistry.counter("event.submitted");

        this.duplicateCounter =
                meterRegistry.counter("event.duplicate");

        this.accountFailureCounter =
                meterRegistry.counter("account.service.failure");
    }

    public void incrementSubmitted() {
        submittedCounter.increment();
    }

    public void incrementDuplicate() {
        duplicateCounter.increment();
    }

    public void incrementAccountFailure() {
        accountFailureCounter.increment();
    }
}
