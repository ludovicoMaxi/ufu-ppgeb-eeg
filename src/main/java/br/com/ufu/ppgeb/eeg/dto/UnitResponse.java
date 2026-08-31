package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a unit.
 *
 * @param id the unit id
 * @param name the unit name
 * @param description the unit description
 */
@Builder
public record UnitResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("name")
    String name,
    @JsonProperty("description")
    String description) {
}