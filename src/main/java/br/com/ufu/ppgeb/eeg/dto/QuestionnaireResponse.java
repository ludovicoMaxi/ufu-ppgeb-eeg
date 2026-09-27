package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a questionnaire.
 *
 * @param id the questionnaire id
 * @param title the questionnaire title
 * @param description the questionnaire description
 * @param questions the questions linked to the questionnaire
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record QuestionnaireResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("title")
    String title,
    @JsonProperty("description")
    String description,
    @JsonProperty("questions")
    List<QuestionnaireQuestionResponse> questions,
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