package com.framework.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventDTO(
    Long id,
    String name,
    Long kickoff, // time in millis
    List<MarketDTO> markets
) {
}
