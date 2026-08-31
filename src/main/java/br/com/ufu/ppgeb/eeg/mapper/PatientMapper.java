package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import br.com.ufu.ppgeb.eeg.dto.PatientResponse;
import br.com.ufu.ppgeb.eeg.model.Patient;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Patient entity and its request/response DTOs.
 */
@UtilityClass
public class PatientMapper {

  /**
   * Builds a Patient reference from its id (null-safe).
   *
   * @param patientId the patient id
   * @return the Patient reference or null if patientId is null
   */
  public static Patient buildReference(Long patientId) {

    if (isNull(patientId)) {
      return null;
    }
    return Patient.builder().id(patientId).build();
  }

  /**
   * Maps a create request to a Patient entity.
   *
   * @param request the create request
   * @return the Patient entity
   */
  public static Patient toEntity(PatientRequest request) {

    return toEntity(request, null);
  }

  /**
   * Maps an update request to a Patient entity preserving the id.
   *
   * @param request the update request
   * @param id the patient id
   * @return the Patient entity
   */
  public static Patient toEntity(PatientRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }
    return Patient.builder()
        .id(id)
        .name(request.name())
        .documentNumber(request.documentNumber())
        .sex(request.sex())
        .birthDate(request.birthDate())
        .nationality(request.nationality())
        .civilStatus(request.civilStatus())
        .job(request.job())
        .build();
  }

  /**
   * Maps a Patient entity to a response DTO.
   *
   * @param patient the Patient entity
   * @return the response DTO
   */
  public static PatientResponse toResponse(Patient patient) {

    if (isNull(patient)) {
      return null;
    }
    return PatientResponse.builder()
        .id(patient.getId())
        .name(patient.getName())
        .documentNumber(patient.getDocumentNumber())
        .sex(patient.getSex())
        .birthDate(patient.getBirthDate())
        .nationality(patient.getNationality())
        .civilStatus(patient.getCivilStatus())
        .job(patient.getJob())
        .createdAt(patient.getCreatedAt())
        .createdBy(patient.getCreatedBy())
        .updatedAt(patient.getUpdatedAt())
        .updatedBy(patient.getUpdatedBy())
        .build();
  }
}
