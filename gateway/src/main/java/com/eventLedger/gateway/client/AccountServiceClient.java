package com.eventLedger.gateway.client;


import com.eventLedger.gateway.dto.AccountTransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AccountServiceClient {

    private final RestTemplate restTemplate;

    @Value("${account.service.base-url}")
    private String accountServiceUrl;

    public void applyTransaction(String accountId,
                                 AccountTransactionRequest request) {

        String url =
                accountServiceUrl +
                        "/accounts/" +
                        accountId +
                        "/transactions";

        restTemplate.postForObject(
                url,
                request,
                Void.class
        );
    }
}
