package com.framework.service;

import com.framework.client.LeonApiClient;
import com.framework.client.dto.EventDTO;
import com.framework.client.dto.LeagueDTO;
import com.framework.client.dto.RegionDTO;
import com.framework.client.dto.SportDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParserService implements CommandLineRunner {

    private final LeonApiClient apiClient;
    private final AsyncMatchProcessor matchProcessor;

    @Value("${leon.target-sports}")
    private List<String> targetSports;

    public ParserService(LeonApiClient apiClient, AsyncMatchProcessor matchProcessor) {
        this.apiClient = apiClient;
        this.matchProcessor = matchProcessor;
    }

    @Override
    public void run(String... args) {
        parse();
    }

    public void parse() {
        List<SportDTO> sports = apiClient.getSports();
        if (sports == null) return;

        for (SportDTO sport : sports) {
            if (!targetSports.contains(sport.name())) continue;

            System.out.println(">>> Checking Sport: " + sport.name());

            if (sport.regions() == null) continue;

            for (RegionDTO region : sport.regions()) {
                if (region.leagues() == null) continue;

                for (LeagueDTO league : region.leagues()) {
                    if (league.top()) {

                        System.out.println("  Found Top League: " + league.name());

                        List<EventDTO> events = apiClient.getEventsByLeague(league.id());

                        if (events != null && !events.isEmpty()) {
                            events.stream()
                                .limit(2)
                                .forEach(event -> {
                                    matchProcessor.processMatch(event.id(), sport.name(), league.name());
                                });
                        }
                    }
                }
            }
        }
    }
}
