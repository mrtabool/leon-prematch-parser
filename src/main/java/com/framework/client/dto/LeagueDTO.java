package com.framework.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueDTO(
    Long id,
    String name,
    boolean top
) {
}
