package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

/**
 * Response DTO for an exam.
 *
 * @param id the exam id
 * @param patientId the patient id
 * @param examRequestId the exam request id
 * @param achievementDate the achievement date
 * @param medicalReport the medical report
 * @param conclusion the conclusion
 * @param bed the bed
 * @param height the height
 * @param weight the weight
 * @param clinicalData the clinical data
 * @param examMedicaments the exam medicaments
 * @param examEquipments the exam equipments
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record ExamResponse(
    Long id,
    Long patientId,
    Long examRequestId,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime achievementDate,
    String medicalReport,
    String conclusion,
    String bed,
    Long height,
    Double weight,
    String clinicalData,
    List<ExamMedicamentResponse> examMedicaments,
    List<ExamEquipmentResponse> examEquipments,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime createdAt,
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime updatedAt,
    String updatedBy) {
}
