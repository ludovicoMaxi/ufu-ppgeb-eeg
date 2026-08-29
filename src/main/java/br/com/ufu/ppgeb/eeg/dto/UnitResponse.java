package br.com.ufu.ppgeb.eeg.dto;

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
    Long id,
    String name,
    String description) {
}
