package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.MedicamentResponse;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Medicament entity and its response DTO.
 */
@UtilityClass
public class MedicamentMapper {

  /**
   * Maps a Medicament entity to a response DTO.
   *
   * @param medicament the Medicament entity
   * @return the response DTO
   */
  public static MedicamentResponse toResponse(Medicament medicament) {

    if (isNull(medicament)) {
      return null;
    }
    return MedicamentResponse.builder()
        .id(medicament.getId())
        .name(medicament.getName())
        .description(medicament.getDescription())
        .createdAt(medicament.getCreatedAt())
        .createdBy(medicament.getCreatedBy())
        .updatedAt(medicament.getUpdatedAt())
        .updatedBy(medicament.getUpdatedBy())
        .build();
  }
}
