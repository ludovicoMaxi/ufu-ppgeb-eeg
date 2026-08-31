package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import br.com.ufu.ppgeb.eeg.dto.ExamRequestRequest;
import br.com.ufu.ppgeb.eeg.dto.ExamRequestResponse;
import br.com.ufu.ppgeb.eeg.model.ExamRequest;
import br.com.ufu.ppgeb.eeg.model.Patient;
import lombok.experimental.UtilityClass;

/**
 * Maps between the ExamRequest entity and its request/response DTOs.
 */
@UtilityClass
public class ExamRequestMapper {

  /**
   * Builds an ExamRequest reference from its id (null-safe).
   *
   * @param examRequestId the exam request id
   * @return the ExamRequest reference or null if examRequestId is null
   */
  public static ExamRequest buildReference(Long examRequestId) {

    if (isNull(examRequestId)) {
      return null;
    }
    return ExamRequest.builder().id(examRequestId).build();
  }

  /**
   * Maps a create request to an ExamRequest entity.
   *
   * @param request the create request
   * @return the ExamRequest entity
   */
  public static ExamRequest toEntity(ExamRequestRequest request) {

    return toEntity(request, null);
  }

  /**
   * Maps an update request to an ExamRequest entity preserving the id.
   *
   * @param request the update request
   * @param id the exam request id
   * @return the ExamRequest entity
   */
  public static ExamRequest toEntity(ExamRequestRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }

    final Patient patient = PatientMapper.buildReference(request.patientId());

    return ExamRequest.builder()
        .id(id)
        .medicalRecord(request.medicalRecord())
        .medicalRequest(request.medicalRequest())
        .sector(request.sector())
        .agreement(request.agreement())
        .doctorRequestant(request.doctorRequestant())
        .user(request.user())
        .clinicOrigin(request.clinicOrigin())
        .cityOrigin(request.cityOrigin())
        .patient(patient)
        .requestDate(request.requestDate())
        .achievementDate(request.achievementDate())
        .build();
  }

  /**
   * Maps an ExamRequest entity to a response DTO.
   *
   * @param examRequest the ExamRequest entity
   * @return the response DTO
   */
  public static ExamRequestResponse toResponse(ExamRequest examRequest) {

    if (isNull(examRequest)) {
      return null;
    }
    Long patientId = null;
    if (nonNull(examRequest.getPatient())) {
      patientId = examRequest.getPatient().getId();
    }

    return ExamRequestResponse.builder()
        .id(examRequest.getId())
        .medicalRecord(examRequest.getMedicalRecord())
        .medicalRequest(examRequest.getMedicalRequest())
        .sector(examRequest.getSector())
        .agreement(examRequest.getAgreement())
        .doctorRequestant(examRequest.getDoctorRequestant())
        .user(examRequest.getUser())
        .clinicOrigin(examRequest.getClinicOrigin())
        .cityOrigin(examRequest.getCityOrigin())
        .patientId(patientId)
        .requestDate(examRequest.getRequestDate())
        .achievementDate(examRequest.getAchievementDate())
        .createdAt(examRequest.getCreatedAt())
        .createdBy(examRequest.getCreatedBy())
        .updatedAt(examRequest.getUpdatedAt())
        .updatedBy(examRequest.getUpdatedBy())
        .build();
  }
}
