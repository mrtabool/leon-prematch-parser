package com.framework.service;

import com.framework.client.LeonApiClient;
import com.framework.client.dto.EventDTO;
import com.framework.client.dto.MarketDTO;
import com.framework.client.dto.SelectionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncMatchProcessorTest {

    @Mock
    private LeonApiClient apiClient;

    @InjectMocks
    private AsyncMatchProcessor matchProcessor;

    @Test
    void testProcessMatchFormatting() {
        Long eventId = 123L;

        List<SelectionDTO> selections = List.of(
            new SelectionDTO(1001L, "1", 1.50),
            new SelectionDTO(1002L, "2", 2.50)
        );

        MarketDTO market = new MarketDTO(1L, "Winner", selections);

        EventDTO mockEvent = new EventDTO(
            eventId,
            "Team A - Team B",
            1707127200000L,
            List.of(market)
        );

        when(apiClient.getFullEventDetails(eventId)).thenReturn(mockEvent);

        matchProcessor.processMatch(eventId, "Football", "Premier League").join();

        verify(apiClient).getFullEventDetails(eventId);
    }
}
