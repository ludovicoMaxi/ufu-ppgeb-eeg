package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.QuestionAnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import lombok.experimental.UtilityClass;

/**
 * Maps between the QuestionAnswerOption entity and its request/response DTOs.
 */
@UtilityClass
public class QuestionAnswerOptionMapper {

  /**
   * Maps a request to a QuestionAnswerOption entity setting the question.
   *
   * @param request the request
   * @param question the question
   * @return the QuestionAnswerOption entity
   */
  public static QuestionAnswerOption toDomain(QuestionAnswerOptionRequest request,
      Question question) {

    if (isNull(request)) {
      return null;
    }
    return QuestionAnswerOption.builder()
        .question(question)
        .answerOption(AnswerOptionMapper.buildReference(request.answerOptionId()))
        .sortOrder(request.sortOrder())
        .build();
  }

  /**
   * Maps a QuestionAnswerOption entity to a response DTO.
   *
   * @param entity the QuestionAnswerOption entity
   * @return the response DTO
   */
  public static QuestionAnswerOptionResponse toResponse(QuestionAnswerOption entity) {

    if (isNull(entity)) {
      return null;
    }
    return QuestionAnswerOptionResponse.builder()
        .id(entity.getId())
        .questionId(entity.getQuestionId())
        .answerOption(AnswerOptionMapper.toResponse(entity.getAnswerOption()))
        .sortOrder(entity.getSortOrder())
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }
}