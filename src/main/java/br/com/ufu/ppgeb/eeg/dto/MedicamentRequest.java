package br.com.ufu.ppgeb.eeg.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for medicament inside exam medicament.
 *
 * @param id the medicament id (null when creating new "outro")
 * @param name the medicament name
 * @param description the medicament description
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MedicamentRequest(
    @JsonProperty("id")
    Long id,
    @NotBlank
    @Size(max = 256)
    @JsonProperty("name")
    String name,
    @Size(max = 256)
    @JsonProperty("description")
    String description) {
}