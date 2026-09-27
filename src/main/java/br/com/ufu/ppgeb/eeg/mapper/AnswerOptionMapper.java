package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.AnswerOptionRequest;
import br.com.ufu.ppgeb.eeg.dto.AnswerOptionResponse;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import lombok.experimental.UtilityClass;

/**
 * Maps between the AnswerOption entity and its request/response DTOs.
 */
@UtilityClass
public class AnswerOptionMapper {

  /**
   * Builds an AnswerOption reference from its id (null-safe).
   *
   * @param answerOptionId the answer option id
   * @return the AnswerOption reference or null if answerOptionId is null
   */
  public static AnswerOption buildReference(Long answerOptionId) {

    if (isNull(answerOptionId)) {
      return null;
    }
    return AnswerOption.builder().id(answerOptionId).build();
  }

  /**
   * Maps a create request to an AnswerOption entity.
   *
   * @param request the request
   * @return the AnswerOption entity
   */
  public static AnswerOption toDomain(AnswerOptionRequest request) {

    return toDomain(request, null);
  }

  /**
   * Maps an update request to an AnswerOption entity preserving the id.
   *
   * @param request the request
   * @param id the answer option id
   * @return the AnswerOption entity
   */
  public static AnswerOption toDomain(AnswerOptionRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }

    Long answerOptionId = isNull(id) ? request.id() : id;

    return AnswerOption.builder()
        .id(answerOptionId)
        .text(request.text())
        .active(request.active())
        .build();
  }

  /**
   * Maps an AnswerOption entity to a response DTO.
   *
   * @param entity the AnswerOption entity
   * @return the response DTO
   */
  public static AnswerOptionResponse toResponse(AnswerOption entity) {

    if (isNull(entity)) {
      return null;
    }
    return AnswerOptionResponse.builder()
        .id(entity.getId())
        .text(entity.getText())
        .active(entity.getActive())
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }
}