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
  public static Activity toEntity(ActivityRequest request, Long examId) {

    if (isNull(request)) {
      return null;
    }
    return toEntity(request, request.id(), examId);
  }

  /**
   * Maps a request to an Activity entity preserving the id and setting the exam id.
   *
   * @param request the request
   * @param id the activity id
   * @param examId the exam id
   * @return the Activity entity
   */
  public static Activity toEntity(ActivityRequest request, Long id, Long examId) {

    if (isNull(request)) {
      return null;
    }
    return Activity.builder()
        .id(id)
        .examId(examId)
        .startTime(request.startTime())
        .duration(request.duration())
        .description(request.description())
        .build();
  }

  /**
   * Maps an activity response to an Activity entity.
   *
   * @param response the activity response
   * @return the Activity entity
   */
  public static Activity toEntity(ActivityResponse response) {

    if (isNull(response)) {
      return null;
    }
    return Activity.builder()
        .id(response.id())
        .examId(response.examId())
        .startTime(response.startTime())
        .duration(response.duration())
        .description(response.description())
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
