package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;
import java.util.List;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    Long id,
    @JsonProperty("patientId")
    Long patientId,
    @JsonProperty("examRequestId")
    Long examRequestId,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("achievementDate")
    ZonedDateTime achievementDate,
    @JsonProperty("medicalReport")
    String medicalReport,
    @JsonProperty("conclusion")
    String conclusion,
    @JsonProperty("bed")
    String bed,
    @JsonProperty("height")
    Long height,
    @JsonProperty("weight")
    Double weight,
    @JsonProperty("clinicalData")
    String clinicalData,
    @JsonProperty("examMedicaments")
    List<ExamMedicamentResponse> examMedicaments,
    @JsonProperty("examEquipments")
    List<ExamEquipmentResponse> examEquipments,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("createdAt")
    ZonedDateTime createdAt,
    @JsonProperty("createdBy")
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("updatedAt")
    ZonedDateTime updatedAt,
    @JsonProperty("updatedBy")
    String updatedBy) {
}