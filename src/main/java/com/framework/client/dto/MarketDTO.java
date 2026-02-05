package com.framework.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketDTO(
    Long id,
    String name,
    @JsonProperty("runners")
    List<SelectionDTO> selections
) {
    public MarketDTO {
        if (selections == null) selections = List.of();
    }
}
