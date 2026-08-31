package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an exam.
 *
 * @param id the exam id (used on update)
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
 */
public record ExamRequest(
    Long id,
    @NotNull
    Long patientId,
    Long examRequestId,
    @NotNull
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime achievementDate,
    @Size(max = 256)
    String medicalReport,
    @Size(max = 256)
    String conclusion,
    @Size(max = 256)
    String bed,
    Long height,
    Double weight,
    @Size(max = 256)
    String clinicalData,
    List<ExamMedicamentRequest> examMedicaments,
    List<ExamEquipmentRequest> examEquipments) {
}
