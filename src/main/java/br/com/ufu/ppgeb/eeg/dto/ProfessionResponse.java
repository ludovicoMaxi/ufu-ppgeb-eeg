package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a profession.
 *
 * @param id the profession id
 * @param name the profession name
 */
@Builder
public record ProfessionResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("name")
    String name) {
}
