package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a questionnaire answer.
 *
 * @param id the answer id
 * @param questionnaireId the questionnaire id
 * @param questionId the questioned question id
 * @param objectType the object type
 * @param objectId the object id
 * @param freeText the free text answer
 * @param answerOptions the selected catalog answer options
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record QuestionnaireAnswerResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("questionnaireId")
    Long questionnaireId,
    @JsonProperty("questionId")
    Long questionId,
    @JsonProperty("objectType")
    Long objectType,
    @JsonProperty("objectId")
    Long objectId,
    @JsonProperty("freeText")
    String freeText,
    @JsonProperty("answerOptions")
    List<AnswerOptionResponse> answerOptions,
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