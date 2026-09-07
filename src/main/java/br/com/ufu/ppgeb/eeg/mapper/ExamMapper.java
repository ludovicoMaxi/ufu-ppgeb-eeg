package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamEquipmentResponse;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamMedicamentResponse;
import br.com.ufu.ppgeb.eeg.dto.ExamRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamResponse;
import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.model.ExamEquipment;
import br.com.ufu.ppgeb.eeg.model.ExamMedicament;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Exam entity and its request/response DTOs.
 */
@UtilityClass
public class ExamMapper {

  /**
   * Builds an Exam reference from its id (null-safe).
   *
   * @param examId the exam id
   * @return the Exam reference or null if examId is null
   */
  public static Exam buildReference(Long examId) {

    if (isNull(examId)) {
      return null;
    }
    return Exam.builder().id(examId).build();
  }

  /**
   * Maps a create request to an Exam entity.
   *
   * @param request the create request
   * @return the Exam entity
   */
  public static Exam toDomain(ExamRequest request) {

    return toDomain(request, null);
  }

  /**
   * Maps an update request to an Exam entity preserving the id.
   *
   * @param request the update request
   * @param id the exam id
   * @return the Exam entity
   */
  public static Exam toDomain(ExamRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }

    Long examId = isNull(id) ? request.id() : id;

    return Exam.builder()
        .id(examId)
        .patient(PatientMapper.buildReference(request.patientId()))
        .examRequest(ExamRequestMapper.buildReference(request.examRequestId()))
        .achievementDate(request.achievementDate())
        .medicalReport(request.medicalReport())
        .conclusion(request.conclusion())
        .bed(request.bed())
        .height(request.height())
        .weight(request.weight())
        .clinicalData(request.clinicalData())
        .examMedicaments(mapMedicaments(request.examMedicaments(), examId))
        .examEquipments(mapEquipments(request.examEquipments(), examId))
        .build();
  }

  private static List<ExamMedicament> mapMedicaments(List<ExamMedicamentRequest> medicaments,
      Long examId) {

    if (isNull(medicaments)) {
      return null;
    }
    return medicaments.stream()
        .filter(medicament -> nonNull(medicament))
        .map(medicament -> ExamMedicamentMapper.toDomain(medicament, examId))
        .toList();
  }

  private static List<ExamEquipment> mapEquipments(List<ExamEquipmentRequest> equipments,
      Long examId) {

    if (isNull(equipments)) {
      return null;
    }
    return equipments.stream()
        .filter(equipment -> nonNull(equipment))
        .map(equipment -> ExamEquipmentMapper.toDomain(equipment, examId))
        .toList();
  }

  /**
   * Maps an Exam entity to a response DTO.
   *
   * @param exam the Exam entity
   * @return the response DTO
   */
  public static ExamResponse toResponse(Exam exam) {

    if (isNull(exam)) {
      return null;
    }

    Long patientId = null;
    if (nonNull(exam.getPatient())) {
      patientId = exam.getPatient().getId();
    }

    Long examRequestId = null;
    if (nonNull(exam.getExamRequest())) {
      examRequestId = exam.getExamRequest().getId();
    }

    return ExamResponse.builder()
        .id(exam.getId())
        .patientId(patientId)
        .examRequestId(examRequestId)
        .achievementDate(exam.getAchievementDate())
        .medicalReport(exam.getMedicalReport())
        .conclusion(exam.getConclusion())
        .bed(exam.getBed())
        .height(exam.getHeight())
        .weight(exam.getWeight())
        .clinicalData(exam.getClinicalData())
        .examMedicaments(mapMedicamentResponses(exam.getExamMedicaments()))
        .examEquipments(mapEquipmentResponses(exam.getExamEquipments()))
        .createdAt(exam.getCreatedAt())
        .createdBy(exam.getCreatedBy())
        .updatedAt(exam.getUpdatedAt())
        .updatedBy(exam.getUpdatedBy())
        .build();
  }

  private static List<ExamMedicamentResponse> mapMedicamentResponses(
      List<ExamMedicament> examMedicaments) {

    if (isNull(examMedicaments)) {
      return null;
    }
    return examMedicaments.stream()
        .filter(medicament -> nonNull(medicament))
        .map(ExamMedicamentMapper::toResponse)
        .toList();
  }

  private static List<ExamEquipmentResponse> mapEquipmentResponses(
      List<ExamEquipment> examEquipments) {

    if (isNull(examEquipments)) {
      return null;
    }
    return examEquipments.stream()
        .filter(equipment -> nonNull(equipment))
        .map(ExamEquipmentMapper::toResponse)
        .toList();
  }
}
