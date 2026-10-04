package br.com.ufu.ppgeb.eeg.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for saving or updating a questionnaire answer.
 *
 * @param id the answer id (present on update)
 * @param questionId the questioned question id
 * @param freeText the free text answer (required by FREE_TEXT questions and used as the
 *     description/observation of a SINGLE_SELECT or MULTI_SELECT answer)
 * @param answerOptionIds the selected catalog answer options
 */
public record QuestionnaireAnswerRequest(
    @JsonProperty("id")
    Long id,
    @NotNull
    @JsonProperty("questionId")
    Long questionId,
    @Size(max = 2048)
    @JsonProperty("freeText")
    String freeText,
    @JsonProperty("answerOptionIds")
    List<@NotNull Long> answerOptionIds) {
}