package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for equipment inside exam equipment.
 *
 * @param id the equipment id (null when creating new "outro")
 * @param name the equipment name
 * @param description the equipment description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipmentRequest(
    Long id,
    @NotBlank
    @Size(max = 256)
    String name,
    @Size(max = 256)
    String description) {
}
