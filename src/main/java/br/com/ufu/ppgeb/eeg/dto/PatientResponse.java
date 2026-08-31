package br.com.ufu.ppgeb.eeg.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for a patient.
 *
 * @param id the patient id
 * @param name the patient name
 * @param documentNumber the patient document number
 * @param sex the patient sex
 * @param birthDate the patient birth date
 * @param nationality the patient nationality
 * @param civilStatus the patient civil status
 * @param job the patient job
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record PatientResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("name")
    String name,
    @JsonProperty("documentNumber")
    String documentNumber,
    @JsonProperty("sex")
    Sex sex,
    @JsonFormat(pattern = DateFormats.ISO_DATE)
    @JsonProperty("birthDate")
    LocalDate birthDate,
    @JsonProperty("nationality")
    String nationality,
    @JsonProperty("civilStatus")
    CivilStatus civilStatus,
    @JsonProperty("job")
    String job,
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