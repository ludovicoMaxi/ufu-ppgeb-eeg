package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.AnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionnaireAnswerResponse;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import lombok.experimental.UtilityClass;

/**
 * Maps between the QuestionnaireAnswer entity and its request/response DTOs.
 */
@UtilityClass
public class QuestionnaireAnswerMapper {

  /**
   * Maps a request to a QuestionnaireAnswer entity setting the application context.
   *
   * @param request the request
   * @param questionnaireId the questionnaire id
   * @param objectType the object type
   * @param objectId the object id
   * @return the QuestionnaireAnswer entity
   */
  public static QuestionnaireAnswer toDomain(QuestionnaireAnswerRequest request,
      Long questionnaireId, Long objectType, Long objectId) {

    if (isNull(request)) {
      return null;
    }

    QuestionnaireAnswer answer = QuestionnaireAnswer.builder()
        .id(request.id())
        .questionnaire(QuestionnaireMapper.buildReference(questionnaireId))
        .question(QuestionMapper.buildReference(request.questionId()))
        .objectType(objectType)
        .objectId(objectId)
        .freeText(request.freeText())
        .build();
    answer.setAnswerOptions(mapAnswerOptions(request.answerOptionIds(), answer));
    return answer;
  }

  private static List<QuestionnaireAnswerOption> mapAnswerOptions(
      List<Long> answerOptionIds, QuestionnaireAnswer answer) {

    if (isNull(answerOptionIds)) {
      return null;
    }
    return answerOptionIds.stream()
        .filter(optionId -> nonNull(optionId))
        .map(optionId -> QuestionnaireAnswerOption.builder()
            .questionnaireAnswer(answer)
            .answerOption(AnswerOptionMapper.buildReference(optionId))
            .build())
        .toList();
  }

  /**
   * Maps a QuestionnaireAnswer entity to a response DTO.
   *
   * @param entity the QuestionnaireAnswer entity
   * @return the response DTO
   */
  public static QuestionnaireAnswerResponse toResponse(QuestionnaireAnswer entity) {

    if (isNull(entity)) {
      return null;
    }

    Long questionnaireId = null;
    if (nonNull(entity.getQuestionnaire())) {
      questionnaireId = entity.getQuestionnaire().getId();
    }

    Long questionId = null;
    if (nonNull(entity.getQuestion())) {
      questionId = entity.getQuestion().getId();
    }

    return QuestionnaireAnswerResponse.builder()
        .id(entity.getId())
        .questionnaireId(questionnaireId)
        .questionId(questionId)
        .objectType(entity.getObjectType())
        .objectId(entity.getObjectId())
        .freeText(entity.getFreeText())
        .answerOptions(mapAnswerOptionResponses(entity.getAnswerOptions()))
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }

  private static List<AnswerOptionResponse> mapAnswerOptionResponses(
      List<QuestionnaireAnswerOption> options) {

    if (isNull(options)) {
      return null;
    }
    return options.stream()
        .filter(option -> nonNull(option))
        .map(option -> AnswerOptionMapper.toResponse(option.getAnswerOption()))
        .toList();
  }
}