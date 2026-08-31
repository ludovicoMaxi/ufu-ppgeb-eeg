package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("medicalRecord")
    Long medicalRecord,
    @NotNull
    @JsonProperty("medicalRequest")
    Long medicalRequest,
    @NotBlank
    @Size(max = 256)
    @JsonProperty("sector")
    String sector,
    @JsonProperty("agreement")
    String agreement,
    @NotBlank
    @Size(max = 256)
    @JsonProperty("doctorRequestant")
    String doctorRequestant,
    @NotBlank
    @Size(max = 256)
    @JsonProperty("user")
    String user,
    @JsonProperty("clinicOrigin")
    String clinicOrigin,
    @JsonProperty("cityOrigin")
    String cityOrigin,
    @NotNull
    @JsonProperty("patientId")
    Long patientId,
    @NotNull
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("requestDate")
    ZonedDateTime requestDate,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    @JsonProperty("achievementDate")
    ZonedDateTime achievementDate) {
}