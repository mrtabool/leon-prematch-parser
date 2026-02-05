package com.framework.service;

import com.framework.client.LeonApiClient;
import com.framework.client.dto.EventDTO;
import com.framework.client.dto.MarketDTO;
import com.framework.client.dto.SelectionDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncMatchProcessor {

    private final LeonApiClient apiClient;
    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'").withZone(ZoneOffset.UTC);

    public AsyncMatchProcessor(LeonApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<Void> processMatch(Long eventId, String sportName, String leagueName) {
        try {
            EventDTO details = apiClient.getFullEventDetails(eventId);
            if (details == null || details.markets() == null) return CompletableFuture.completedFuture(null);

            StringBuilder sb = new StringBuilder();

            sb.append(sportName).append(", ").append(leagueName).append("\n");

            String startTime = java.time.Instant.ofEpochMilli(details.kickoff())
                .toString().replace("T", " ").substring(0, 19) + " UTC";

            sb.append(details.name()).append(", ")
                .append(startTime).append(", ")
                .append(details.id()).append("\n");

            for (MarketDTO market : details.markets()) {
                sb.append(market.name()).append("\n");

                if (market.selections() != null) {
                    for (SelectionDTO sel : market.selections()) {
                        sb.append(sel.name()).append(", ")
                            .append(sel.price()).append(", ")
                            .append(sel.id()).append("\n");
                    }
                }
            }

            System.out.println(sb);

        } catch (Exception e) {
            System.err.println("Error processing match " + eventId + ": " + e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
}
