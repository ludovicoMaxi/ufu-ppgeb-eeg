package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for the link between a question and a catalog answer option.
 *
 * @param answerOptionId the catalog answer option id
 * @param sortOrder the option order within the question
 */
public record QuestionAnswerOptionRequest(
    @NotNull
    @JsonProperty("answerOptionId")
    Long answerOptionId,
    @JsonProperty("sortOrder")
    Long sortOrder) {
}