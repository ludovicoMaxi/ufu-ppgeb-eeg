package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a question of the questionnaire catalog.
 *
 * @param id the question id
 * @param text the question text
 * @param description the question description
 * @param questionType the question type
 * @param active whether the question is active
 * @param options the answer options linked to the question
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record QuestionResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("text")
    String text,
    @JsonProperty("description")
    String description,
    @JsonProperty("questionType")
    QuestionType questionType,
    @JsonProperty("active")
    Boolean active,
    @JsonProperty("options")
    List<QuestionAnswerOptionResponse> options,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("createdAt")
    ZonedDateTime createdAt,
    @JsonProperty("createdBy")
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("updatedAt")
    ZonedDateTime updatedAt,
    @JsonProperty("updatedBy")
    String updatedBy) {
}