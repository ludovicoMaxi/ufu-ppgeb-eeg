package br.com.ufu.ppgeb.eeg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an activity.
 *
 * @param id the activity id (used on update)
 * @param startTime the start time
 * @param duration the duration
 * @param description the description
 */
public record ActivityRequest(
    Long id,
    @NotNull
    Long startTime,
    @NotNull
    Long duration,
    @NotBlank
    @Size(max = 1024)
    String description) {
}
