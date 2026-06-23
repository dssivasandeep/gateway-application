package com.eventLedger.gateway.client;


import com.eventLedger.gateway.dto.AccountTransactionRequest;
import com.eventLedger.gateway.exception.AccountServiceUnavailableException;
import com.eventLedger.gateway.trace.TraceContext;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AccountServiceClient {

    private final RestTemplate restTemplate;

    @Value("${account.service.base-url}")
    private String accountServiceUrl;

    @CircuitBreaker(
            name = "accountService",
            fallbackMethod = "fallbackApplyTransaction"
    )
    public void applyTransaction(String accountId,
                                 AccountTransactionRequest request) {

        String url =
                accountServiceUrl +
                        "/accounts/" +
                        accountId +
                        "/transactions";

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                "X-Trace-Id",
                TraceContext.getTraceId()
        );

        HttpEntity<AccountTransactionRequest> entity =
                new HttpEntity<>(request, headers);

        restTemplate.postForObject(
                url,
                entity,
                Void.class
        );
    }

    public void fallbackApplyTransaction(
            String accountId,
            AccountTransactionRequest request,
            Exception ex) {

        throw new AccountServiceUnavailableException(
                "Account Service is unavailable"
        );
    }
}