package com.framework.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SportDTO(
    Long id,
    String name,
    List<RegionDTO> regions
) {
}
