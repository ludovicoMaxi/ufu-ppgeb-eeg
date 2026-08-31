package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating or updating an exam equipment.
 *
 * @param id the exam equipment id (used on update)
 * @param equipment the equipment (id may be null for new "outro")
 * @param amount the amount
 * @param unitId the unit id
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ExamEquipmentRequest(
    Long id,
    @NotNull
    EquipmentRequest equipment,
    @NotNull
    Long amount,
    @NotNull
    Long unitId) {
}