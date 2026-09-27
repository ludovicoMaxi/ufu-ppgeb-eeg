package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireQuestionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireResponse;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Questionnaire entity and its request/response DTOs.
 */
@UtilityClass
public class QuestionnaireMapper {

  /**
   * Builds a Questionnaire reference from its id (null-safe).
   *
   * @param questionnaireId the questionnaire id
   * @return the Questionnaire reference or null if questionnaireId is null
   */
  public static Questionnaire buildReference(Long questionnaireId) {

    if (isNull(questionnaireId)) {
      return null;
    }
    return Questionnaire.builder().id(questionnaireId).build();
  }

  /**
   * Maps a create request to a Questionnaire entity with its question links.
   *
   * @param request the request
   * @return the Questionnaire entity
   */
  public static Questionnaire toDomain(QuestionnaireRequest request) {

    return toDomain(request, null);
  }

  /**
   * Maps an update request to a Questionnaire entity preserving the id.
   *
   * @param request the request
   * @param id the questionnaire id
   * @return the Questionnaire entity
   */
  public static Questionnaire toDomain(QuestionnaireRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }

    Long questionnaireId = isNull(id) ? request.id() : id;

    Questionnaire questionnaire = Questionnaire.builder()
        .id(questionnaireId)
        .title(request.title())
        .description(request.description())
        .build();
    questionnaire.setQuestionnaireQuestions(
        mapQuestionLinks(request.questions(), questionnaire));
    return questionnaire;
  }

  private static List<QuestionnaireQuestion> mapQuestionLinks(
      List<QuestionnaireQuestionRequest> questions, Questionnaire questionnaire) {

    if (isNull(questions)) {
      return null;
    }
    return questions.stream()
        .filter(question -> nonNull(question))
        .map(question -> QuestionnaireQuestionMapper.toDomain(question, questionnaire))
        .toList();
  }

  /**
   * Maps a Questionnaire entity to a response DTO.
   *
   * @param entity the Questionnaire entity
   * @return the response DTO
   */
  public static QuestionnaireResponse toResponse(Questionnaire entity) {

    if (isNull(entity)) {
      return null;
    }
    return QuestionnaireResponse.builder()
        .id(entity.getId())
        .title(entity.getTitle())
        .description(entity.getDescription())
        .questions(mapQuestionLinkResponses(entity.getQuestionnaireQuestions()))
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }

  private static List<QuestionnaireQuestionResponse> mapQuestionLinkResponses(
      List<QuestionnaireQuestion> questions) {

    if (isNull(questions)) {
      return null;
    }
    return questions.stream()
        .filter(question -> nonNull(question))
        .map(QuestionnaireQuestionMapper::toResponse)
        .toList();
  }
}