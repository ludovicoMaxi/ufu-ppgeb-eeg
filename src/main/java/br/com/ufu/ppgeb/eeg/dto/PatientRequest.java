package br.com.ufu.ppgeb.eeg.dto;

import java.time.LocalDate;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Sex;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a patient.
 *
 * @param name the patient name
 * @param documentNumber the patient document number
 * @param sex the patient sex
 * @param birthDate the patient birth date
 * @param nationality the patient nationality
 * @param civilStatus the patient civil status
 * @param job the patient job
 */
public record PatientRequest(
    @NotBlank
    @Size(max = 512)
    @JsonProperty("name")
    String name,
    @NotBlank
    @Size(max = 20)
    @JsonProperty("documentNumber")
    String documentNumber,
    @JsonProperty("sex")
    Sex sex,
    @NotNull
    @JsonFormat(pattern = DateFormats.ISO_DATE)
    @JsonProperty("birthDate")
    LocalDate birthDate,
    @NotBlank
    @Size(max = 20)
    @JsonProperty("nationality")
    String nationality,
    @JsonProperty("civilStatus")
    CivilStatus civilStatus,
    @Size(max = 256)
    @JsonProperty("job")
    String job) {
}