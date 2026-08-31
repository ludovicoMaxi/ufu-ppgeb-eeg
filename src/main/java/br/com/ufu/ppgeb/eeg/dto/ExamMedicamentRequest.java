package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating or updating an exam medicament.
 *
 * @param id the exam medicament id (used on update)
 * @param medicament the medicament (id may be null for new "outro")
 * @param amount the amount
 * @param unitId the unit id
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExamMedicamentRequest(
    @JsonProperty("id")
    Long id,
    @NotNull
    @JsonProperty("medicament")
    MedicamentRequest medicament,
    @NotNull
    @JsonProperty("amount")
    Long amount,
    @NotNull
    @JsonProperty("unitId")
    Long unitId) {
}