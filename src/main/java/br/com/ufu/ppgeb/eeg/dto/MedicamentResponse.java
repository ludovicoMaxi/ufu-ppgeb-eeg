package br.com.ufu.ppgeb.eeg.dto;

import java.time.ZonedDateTime;

import br.com.ufu.ppgeb.eeg.constant.DateFormats;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

/**
 * Response DTO for a medicament.
 *
 * @param id the medicament id
 * @param name the medicament name
 * @param description the medicament description
 * @param createdAt the creation auditing timestamp
 * @param createdBy the creation auditing user
 * @param updatedAt the last update auditing timestamp
 * @param updatedBy the last update auditing user
 */
@Builder
public record MedicamentResponse(
    Long id,
    String name,
    String description,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime createdAt,
    String createdBy,
    @JsonFormat(pattern = DateFormats.ISO_DATE_TIME)
    ZonedDateTime updatedAt,
    String updatedBy) {
}
