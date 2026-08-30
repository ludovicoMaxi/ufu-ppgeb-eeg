package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.EpochRequest;
import br.com.ufu.ppgeb.eeg.dto.EpochResponse;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Epoch entity and its request/response DTOs.
 */
@UtilityClass
public class EpochMapper {

  /**
   * Maps a request to an Epoch entity setting the exam id.
   *
   * @param request the request
   * @param examId the exam id
   * @return the Epoch entity
   */
  public static Epoch toEntity(EpochRequest request, Long examId) {

    if (isNull(request)) {
      return null;
    }
    return Epoch.builder()
        .id(request.id())
        .examId(examId)
        .startTime(request.startTime())
        .duration(request.duration())
        .description(request.description())
        .build();
  }

  /**
   * Maps an Epoch entity to a response DTO.
   *
   * @param epoch the Epoch entity
   * @return the response DTO
   */
  public static EpochResponse toResponse(Epoch epoch) {

    if (isNull(epoch)) {
      return null;
    }
    return EpochResponse.builder()
        .id(epoch.getId())
        .examId(epoch.getExamId())
        .startTime(epoch.getStartTime())
        .duration(epoch.getDuration())
        .description(epoch.getDescription())
        .createdAt(epoch.getCreatedAt())
        .createdBy(epoch.getCreatedBy())
        .updatedAt(epoch.getUpdatedAt())
        .updatedBy(epoch.getUpdatedBy())
        .build();
  }
}
