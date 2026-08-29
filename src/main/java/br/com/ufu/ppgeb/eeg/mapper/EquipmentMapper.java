package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.EquipmentResponse;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Equipment entity and its response DTO.
 */
@UtilityClass
public class EquipmentMapper {

  /**
   * Maps an Equipment entity to a response DTO.
   *
   * @param equipment the Equipment entity
   * @return the response DTO
   */
  public static EquipmentResponse toResponse(Equipment equipment) {

    if (isNull(equipment)) {
      return null;
    }
    return EquipmentResponse.builder()
        .id(equipment.getId())
        .name(equipment.getName())
        .description(equipment.getDescription())
        .createdAt(equipment.getCreatedAt())
        .createdBy(equipment.getCreatedBy())
        .updatedAt(equipment.getUpdatedAt())
        .updatedBy(equipment.getUpdatedBy())
        .build();
  }
}
