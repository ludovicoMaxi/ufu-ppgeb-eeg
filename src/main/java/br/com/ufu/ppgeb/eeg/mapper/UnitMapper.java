package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.UnitResponse;
import br.com.ufu.ppgeb.eeg.model.Unit;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Unit entity and its response DTO.
 */
@UtilityClass
public class UnitMapper {

  /**
   * Maps a Unit entity to a response DTO.
   *
   * @param unit the Unit entity
   * @return the response DTO
   */
  public static UnitResponse toResponse(Unit unit) {

    if (isNull(unit)) {
      return null;
    }
    return UnitResponse.builder()
        .id(unit.getId())
        .name(unit.getName())
        .description(unit.getDescription())
        .build();
  }
}
