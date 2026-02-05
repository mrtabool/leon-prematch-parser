package com.framework.client;

import com.framework.client.dto.SportDTO;
import com.framework.service.ParserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureWireMock(port = 8081)
@TestPropertySource(properties = "leon.api-url=http://localhost:8081")
class LeonApiClientIntegrationTest {

    @Autowired
    private LeonApiClient apiClient;

    @MockBean
    private ParserService parserService;

    @Test
    void testGetSportsWithMockServer() {
        stubFor(get(urlPathEqualTo("/betline/sports"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[{\"id\":1, \"name\":\"Football\", \"regions\":[]}]")));

        List<SportDTO> sports = apiClient.getSports();

        assertNotNull(sports);
        assertEquals(1, sports.size());
        assertEquals("Football", sports.get(0).name());
    }
}
