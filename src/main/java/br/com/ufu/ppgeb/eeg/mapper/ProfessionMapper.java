package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.ProfessionResponse;
import br.com.ufu.ppgeb.eeg.model.Profession;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Profession entity and its response DTO.
 */
@UtilityClass
public class ProfessionMapper {

  /**
   * Maps a Profession entity to a response DTO.
   *
   * @param profession the Profession entity
   * @return the response DTO
   */
  public static ProfessionResponse toResponse(Profession profession) {

    if (isNull(profession)) {
      return null;
    }
    return ProfessionResponse.builder()
        .id(profession.getId())
        .name(profession.getName())
        .build();
  }
}
