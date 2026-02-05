package com.framework.service;

import com.framework.client.LeonApiClient;
import com.framework.client.dto.EventDTO;
import com.framework.client.dto.LeagueDTO;
import com.framework.client.dto.RegionDTO;
import com.framework.client.dto.SportDTO;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParserServiceTest {

    @Test
    void testParseLogicFiltering() {
        LeonApiClient client = mock(LeonApiClient.class);
        AsyncMatchProcessor processor = mock(AsyncMatchProcessor.class);

        ParserService service = new ParserService(client, processor);
        ReflectionTestUtils.setField(service, "targetSports", List.of("Football"));

        LeagueDTO topLeague = new LeagueDTO(10L, "Top League", true);

        RegionDTO region = new RegionDTO(100L, "International", List.of(topLeague));

        SportDTO sport = new SportDTO(1000L, "Football", List.of(region));

        when(client.getSports()).thenReturn(List.of(sport));
        when(client.getEventsByLeague(10L)).thenReturn(List.of(
            new EventDTO(1L, "Team A - Team B", 1707127200000L, null),
            new EventDTO(2L, "Team C - Team D", 1707127200000L, null),
            new EventDTO(3L, "Team E - Team F", 1707127200000L, null)
        ));

        service.parse();

        verify(processor, times(2)).processMatch(anyLong(), eq("Football"), eq("Top League"));
    }
}
