package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * Response DTO for an exam medicament.
 *
 * @param id the exam medicament id
 * @param examId the exam id
 * @param medicament the medicament
 * @param amount the amount
 * @param unit the unit
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record ExamMedicamentResponse(
    @JsonProperty("id")
    Long id,
    @JsonProperty("examId")
    Long examId,
    @JsonProperty("medicament")
    MedicamentResponse medicament,
    @JsonProperty("amount")
    Long amount,
    @JsonProperty("unit")
    UnitResponse unit,
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