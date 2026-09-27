package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for the link between a questionnaire and a question.
 *
 * @param id the link id
 * @param questionnaireId the questionnaire id
 * @param question the question
 * @param sortOrder the question order within the questionnaire
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record QuestionnaireQuestionResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("questionnaireId")
    Long questionnaireId,
    @JsonProperty("question")
    QuestionResponse question,
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