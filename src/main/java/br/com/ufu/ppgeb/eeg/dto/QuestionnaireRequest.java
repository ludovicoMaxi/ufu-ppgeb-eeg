package br.com.ufu.ppgeb.eeg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a questionnaire.
 *
 * @param id the questionnaire id (present on update)
 * @param title the questionnaire title
 * @param description the questionnaire description
 * @param questions the questions linked to the questionnaire
 */
public record QuestionnaireRequest(
    @JsonProperty("id")
    Long id,
    @NotBlank
    @Size(max = 256)
    @JsonProperty("title")
    String title,
    @Size(max = 1024)
    @JsonProperty("description")
    String description,
    @Valid
    @JsonProperty("questions")
    List<QuestionnaireQuestionRequest> questions) {
}