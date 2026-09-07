package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.ActivityRequest;
import br.com.ufu.ppgeb.eeg.dto.ActivityResponse;
import br.com.ufu.ppgeb.eeg.model.Activity;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Activity entity and its request/response DTOs.
 */
@UtilityClass
public class ActivityMapper {

  /**
   * Maps a request to an Activity entity setting the exam id.
   *
   * @param request the request
   * @param examId the exam id
   * @return the Activity entity
   */
  public static Activity toDomain(ActivityRequest request, Long examId) {

    if (isNull(request)) {
      return null;
    }
    return Activity.builder()
        .id(request.id())
        .examId(examId)
        .startTime(request.startTime())
        .duration(request.duration())
        .description(request.description())
        .build();
  }

  /**
   * Maps an Activity entity to a response DTO.
   *
   * @param activity the Activity entity
   * @return the response DTO
   */
  public static ActivityResponse toResponse(Activity activity) {

    if (isNull(activity)) {
      return null;
    }
    return ActivityResponse.builder()
        .id(activity.getId())
        .examId(activity.getExamId())
        .startTime(activity.getStartTime())
        .duration(activity.getDuration())
        .description(activity.getDescription())
        .createdAt(activity.getCreatedAt())
        .createdBy(activity.getCreatedBy())
        .updatedAt(activity.getUpdatedAt())
        .updatedBy(activity.getUpdatedBy())
        .build();
  }
}
