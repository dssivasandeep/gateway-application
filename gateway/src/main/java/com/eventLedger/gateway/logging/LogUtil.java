package com.eventLedger.gateway.logging;


import com.eventLedger.gateway.trace.TraceContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class LogUtil {

    private LogUtil() {
    }

    public static void info(
            String service,
            String message) {

        log.info(
                "{{\"service\":\"{}\",\"traceId\":\"{}\",\"message\":\"{}\"}}",
                service,
                TraceContext.getTraceId(),
                message
        );
    }
}
