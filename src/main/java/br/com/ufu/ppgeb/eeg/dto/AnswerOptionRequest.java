package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an answer option of the questionnaire catalog.
 *
 * @param id the answer option id (present on update)
 * @param text the answer option text
 * @param active whether the answer option is active
 */
public record AnswerOptionRequest(
    @JsonProperty("id")
    Long id,
    @NotBlank
    @Size(max = 512)
    @JsonProperty("text")
    String text,
    @JsonProperty("active")
    Boolean active) {
}