package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for the link between a question and a catalog answer option.
 *
 * @param id the link id
 * @param questionId the question id
 * @param answerOption the catalog answer option
 * @param sortOrder the option order within the question
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record QuestionAnswerOptionResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("questionId")
    Long questionId,
    @JsonProperty("answerOption")
    AnswerOptionResponse answerOption,
    @JsonProperty("sortOrder")
    Long sortOrder,
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