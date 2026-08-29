package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

/**
 * Response DTO for an exam request.
 *
 * @param id the exam request id
 * @param medicalRecord the medical record
 * @param medicalRequest the medical request
 * @param sector the sector
 * @param agreement the agreement
 * @param doctorRequestant the doctor requestant
 * @param user the requesting user
 * @param clinicOrigin the clinic origin
 * @param cityOrigin the city origin
 * @param patientId the patient id
 * @param requestDate the request date
 * @param achievementDate the achievement date
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record ExamRequestResponse(
    Long id,
    Long medicalRecord,
    Long medicalRequest,
    String sector,
    String agreement,
    String doctorRequestant,
    String user,
    String clinicOrigin,
    String cityOrigin,
    Long patientId,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime requestDate,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime achievementDate,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime createdAt,
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime updatedAt,
    String updatedBy) {
}
