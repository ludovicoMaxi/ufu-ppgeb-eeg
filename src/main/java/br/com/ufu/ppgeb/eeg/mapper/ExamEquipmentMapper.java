package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentResponse;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.model.Unit;
import lombok.experimental.UtilityClass;

/**
 * Maps between the ExamEquipment entity and its request/response DTOs.
 */
@UtilityClass
public class ExamEquipmentMapper {

  /**
   * Maps a request to an ExamEquipment entity setting the exam id.
   *
   * @param request the request
   * @param examId the exam id
   * @return the ExamEquipment entity
   */
  public static ExamEquipment toEntity(ExamEquipmentRequest request, Long examId) {

    if (isNull(request)) {
      return null;
    }

    Equipment equipment = mapEquipment(request);
    Unit unit = UnitMapper.buildReference(request.unitId());
    Exam exam = ExamMapper.buildReference(examId);

    return ExamEquipment.builder()
        .id(request.id())
        .exam(exam)
        .equipment(equipment)
        .amount(request.amount())
        .unit(unit)
        .build();
  }

  private static Equipment mapEquipment(ExamEquipmentRequest request) {

    if (isNull(request.equipment())) {
      return null;
    }
    return Equipment.builder()
        .id(request.equipment().id())
        .name(request.equipment().name())
        .description(request.equipment().description())
        .build();
  }

  /**
   * Maps an ExamEquipment entity to a response DTO.
   *
   * @param entity the ExamEquipment entity
   * @return the response DTO
   */
  public static ExamEquipmentResponse toResponse(ExamEquipment entity) {

    if (isNull(entity)) {
      return null;
    }
    return ExamEquipmentResponse.builder()
        .id(entity.getId())
        .examId(entity.getExamId())
        .equipment(EquipmentMapper.toResponse(entity.getEquipment()))
        .amount(entity.getAmount())
        .unit(UnitMapper.toResponse(entity.getUnit()))
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }
}