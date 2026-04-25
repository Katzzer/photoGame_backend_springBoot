package com.pavelkostal.api.externalApiCalls;

import com.pavelkostal.api.model.PositionStackResponseDataWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class PositionStack {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("http://api.positionstack.com/v1/")
            .build();

    public PositionStackResponseDataWrapper getDataByCity(String accessKey, String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("forward")
                        .queryParam("access_key", accessKey)
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .body(PositionStackResponseDataWrapper.class);
    }

    public PositionStackResponseDataWrapper getDataByGps(String accessKey, String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("reverse")
                        .queryParam("access_key", accessKey)
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .body(PositionStackResponseDataWrapper.class);
    }
}
