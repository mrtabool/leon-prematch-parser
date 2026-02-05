package com.framework.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegionDTO(
    Long id,
    String name,
    List<LeagueDTO> leagues
) {
}
