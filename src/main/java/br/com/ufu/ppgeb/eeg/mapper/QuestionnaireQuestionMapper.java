package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionResponse;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import lombok.experimental.UtilityClass;

/**
 * Maps between the QuestionnaireQuestion entity and its request/response DTOs.
 */
@UtilityClass
public class QuestionnaireQuestionMapper {

  /**
   * Maps a request to a QuestionnaireQuestion entity setting the questionnaire.
   *
   * @param request the request
   * @param questionnaire the questionnaire
   * @return the QuestionnaireQuestion entity
   */
  public static QuestionnaireQuestion toDomain(QuestionnaireQuestionRequest request,
      Questionnaire questionnaire) {

    if (isNull(request)) {
      return null;
    }
    return QuestionnaireQuestion.builder()
        .questionnaire(questionnaire)
        .question(QuestionMapper.buildReference(request.questionId()))
        .sortOrder(request.sortOrder())
        .build();
  }

  /**
   * Maps a QuestionnaireQuestion entity to a response DTO.
   *
   * @param entity the QuestionnaireQuestion entity
   * @return the response DTO
   */
  public static QuestionnaireQuestionResponse toResponse(QuestionnaireQuestion entity) {

    if (isNull(entity)) {
      return null;
    }
    return QuestionnaireQuestionResponse.builder()
        .id(entity.getId())
        .questionnaireId(entity.getQuestionnaireId())
        .question(QuestionMapper.toResponse(entity.getQuestion()))
        .sortOrder(entity.getSortOrder())
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }
}