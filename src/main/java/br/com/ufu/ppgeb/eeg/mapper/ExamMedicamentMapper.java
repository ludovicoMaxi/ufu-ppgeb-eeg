package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentResponse;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.model.Unit;
import lombok.experimental.UtilityClass;

/**
 * Maps between the ExamMedicament entity and its request/response DTOs.
 */
@UtilityClass
public class ExamMedicamentMapper {

  /**
   * Maps a request to an ExamMedicament entity setting the exam id.
   *
   * @param request the request
   * @param examId the exam id
   * @return the ExamMedicament entity
   */
  public static ExamMedicament toDomain(ExamMedicamentRequest request, Long examId) {

    if (isNull(request)) {
      return null;
    }

    Medicament medicament = mapMedicament(request);
    Unit unit = UnitMapper.buildReference(request.unitId());
    Exam exam = ExamMapper.buildReference(examId);

    return ExamMedicament.builder()
        .id(request.id())
        .exam(exam)
        .medicament(medicament)
        .amount(request.amount())
        .unit(unit)
        .build();
  }

  private static Medicament mapMedicament(ExamMedicamentRequest request) {

    if (isNull(request.medicament())) {
      return null;
    }
    return Medicament.builder()
        .id(request.medicament().id())
        .name(request.medicament().name())
        .description(request.medicament().description())
        .build();
  }

  /**
   * Maps an ExamMedicament entity to a response DTO.
   *
   * @param entity the ExamMedicament entity
   * @return the response DTO
   */
  public static ExamMedicamentResponse toResponse(ExamMedicament entity) {

    if (isNull(entity)) {
      return null;
    }
    return ExamMedicamentResponse.builder()
        .id(entity.getId())
        .examId(entity.getExamId())
        .medicament(MedicamentMapper.toResponse(entity.getMedicament()))
        .amount(entity.getAmount())
        .unit(UnitMapper.toResponse(entity.getUnit()))
        .createdAt(entity.getCreatedAt())
        .createdBy(entity.getCreatedBy())
        .updatedAt(entity.getUpdatedAt())
        .updatedBy(entity.getUpdatedBy())
        .build();
  }
}