package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    Long id,
    Long examId,
    MedicamentResponse medicament,
    Long amount,
    UnitResponse unit,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime createdAt,
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime updatedAt,
    String updatedBy) {
}