package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating an exam request.
 *
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
 */
public record ExamRequestRequest(
    @NotNull
    Long medicalRecord,
    @NotNull
    Long medicalRequest,
    @NotBlank
    @Size(max = 256)
    String sector,
    String agreement,
    @NotBlank
    @Size(max = 256)
    String doctorRequestant,
    @NotBlank
    @Size(max = 256)
    String user,
    String clinicOrigin,
    String cityOrigin,
    @NotNull
    Long patientId,
    @NotNull
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime requestDate,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime achievementDate) {
}
