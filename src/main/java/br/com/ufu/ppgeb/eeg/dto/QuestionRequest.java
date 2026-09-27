package br.com.ufu.ppgeb.eeg.dto;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a question of the questionnaire catalog.
 *
 * @param id the question id (present on update)
 * @param text the question text
 * @param description the question description
 * @param questionType the question type
 * @param active whether the question is active
 * @param options the answer options linked to the question
 */
public record QuestionRequest(
    @JsonProperty("id")
    Long id,
    @NotBlank
    @Size(max = 512)
    @JsonProperty("text")
    String text,
    @Size(max = 1024)
    @JsonProperty("description")
    String description,
    @NotNull
    @JsonProperty("questionType")
    QuestionType questionType,
    @JsonProperty("active")
    Boolean active,
    @Valid
    @JsonProperty("options")
    List<QuestionAnswerOptionRequest> options) {
}