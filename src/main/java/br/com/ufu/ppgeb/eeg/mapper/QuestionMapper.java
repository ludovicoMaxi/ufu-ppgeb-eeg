package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.dto.QuestionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionResponse;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Question entity and its request/response DTOs.
 */
@UtilityClass
public class QuestionMapper {

  /**
   * Builds a Question reference from its id (null-safe).
   *
   * @param questionId the question id
   * @return the Question reference or null if questionId is null
   */
  public static Question buildReference(Long questionId) {

    if (isNull(questionId)) {
      return null;
    }
    return Question.builder().id(questionId).build();
  }

  /**
   * Maps a create request to a Question entity with its answer option links.
   *
   * @param request the request
   * @return the Question entity
   */
  public static Question toDomain(QuestionRequest request) {

    return toDomain(request, null);
  }

  /**
   * Maps an update request to a Question entity preserving the id.
   *
   * @param request the request
   * @param id the question id
   * @return the Question entity
   */
  public static Question toDomain(QuestionRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }

    Long questionId = isNull(id) ? request.id() : id;

    Question question = Question.builder()
        .id(questionId)
        .text(request.text())
        .description(request.description())
        .questionType(request.questionType())
        .active(request.active())
        .build();
    question.setQuestionAnswerOptions(mapOptions(request.options(), question));
    return question;
  }

  private static List<QuestionAnswerOption> mapOptions(
      List<QuestionAnswerOptionRequest> options, Question question) {

    if (isNull(options)) {
      return null;
    }
    return options.stream()
        .filter(option -> nonNull(option))
        .map(option -> QuestionAnswerOptionMapper.toDomain(option, question))
        .toList();
  }

  /**
   * Maps a Question entity to a response DTO.
   *
   * @param entity the Question entity
   * @return the response DTO
   */
  public static QuestionResponse toResponse(Question entity) {

    if (isNull(entity)) {
      return null;
    }
    return QuestionResponse.builder()
        .id(entity.getId())
        .text(entity.getText())
        .description(entity.getDescription())
        .questionType(entity.getQuestionType())
        .active(entity.getActive())
        .options(mapOptionResponses(entity.getQuestionAnswerOptions()))
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }

  private static List<QuestionAnswerOptionResponse> mapOptionResponses(
      List<QuestionAnswerOption> options) {

    if (isNull(options)) {
      return null;
    }
    return options.stream()
        .filter(option -> nonNull(option))
        .map(QuestionAnswerOptionMapper::toResponse)
        .toList();
  }
}