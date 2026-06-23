package com.eventLedger.gateway.filter;


import com.eventLedger.gateway.trace.TraceContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class TraceFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        String traceId =
                httpRequest.getHeader("X-Trace-Id");

        if (traceId == null || traceId.isEmpty()) {
            traceId = UUID.randomUUID().toString();
        }

        try {

            TraceContext.setTraceId(traceId);

            chain.doFilter(request, response);

        } finally {

            TraceContext.clear();
        }
    }
}
