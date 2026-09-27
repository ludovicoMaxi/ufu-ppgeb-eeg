package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for the link between a questionnaire and a question.
 *
 * @param questionId the question id
 * @param sortOrder the question order within the questionnaire
 */
public record QuestionnaireQuestionRequest(
    @NotNull
    @JsonProperty("questionId")
    Long questionId,
    @JsonProperty("sortOrder")
    Long sortOrder) {
}