package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an epoch.
 *
 * @param id the epoch id (used on update)
 * @param startTime the start time
 * @param duration the duration
 * @param description the description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EpochRequest(
    Long id,
    @NotNull
    Long startTime,
    @NotNull
    Long duration,
    @NotBlank
    @Size(max = 1024)
    String description) {
}
