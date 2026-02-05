package com.framework.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.client.dto.EventDTO;
import com.framework.client.dto.SportDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LeonApiClient {
    private final RestClient restClient;
    private List<String> cookies = new ArrayList<>();

    public LeonApiClient(RestClient restClient) {
        this.restClient = restClient;
    }

    private void ensureSession() {
        if (!cookies.isEmpty()) return;

        ResponseEntity<Void> response = restClient.get()
            .uri("https://leon.bet/")
            .retrieve()
            .toBodilessEntity();

        List<String> setCookies = response.getHeaders().get("Set-Cookie");
        if (setCookies != null) {
            this.cookies = setCookies;
        }
    }

    public List<SportDTO> getSports() {
        ensureSession();

        return restClient.get()
            .uri("/betline/sports?ctag=en-US&flags=urlv2")
            .headers(h -> {
                if (!cookies.isEmpty()) {
                    h.add("Cookie", cookies.get(0).split(";")[0]);
                }
                h.set("Referer", "https://leon.bet/");
                h.set("X-Requested-With", "XMLHttpRequest");
                h.set("Accept", "application/json");
            })
            .retrieve()
            .body(new ParameterizedTypeReference<List<SportDTO>>() {
            });
    }

    public List<EventDTO> getEventsByLeague(Long leagueId) {
        EventResponseWrapper response = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/betline/events/all")
                .queryParam("ctag", "en-US")
                .queryParam("leagueId", leagueId)
                .build())
            .headers(h -> {
                if (!cookies.isEmpty()) h.add("Cookie", cookies.get(0).split(";")[0]);
                h.set("Referer", "https://leon.bet/");
            })
            .retrieve()
            .body(EventResponseWrapper.class);

        return response != null ? response.getEvents() : List.of();
    }

    public EventDTO getFullEventDetails(Long eventId) {
        try {
            Map<String, Object> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/betline/event/all")
                    .queryParam("ctag", "en-US")
                    .queryParam("eventId", eventId)
                    .build())
                .headers(h -> {
                    if (!cookies.isEmpty()) h.add("Cookie", cookies.get(0).split(";")[0]);
                    h.set("Referer", "https://leon.bet/");
                    h.set("X-Requested-With", "XMLHttpRequest");
                })
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {
                });

            if (response == null) return null;

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            return mapper.convertValue(response, EventDTO.class);

        } catch (Exception e) {
            System.err.println("API error for event " + eventId + ": " + e.getMessage());
            return null;
        }
    }
}
