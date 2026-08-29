package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.UnitResponse;
import br.com.ufu.ppgeb.eeg.model.Unit;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UnitMapperTest {

  @Test
  @DisplayName("Given a unit when mapping to response then map every field")
  void givenUnit_whenToResponse_thenMapEveryField() {
    Unit unit = Instancio.create(Unit.class);

    UnitResponse response = UnitMapper.toResponse(unit);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(unit.getId());
    assertThat(response.name()).isEqualTo(unit.getName());
    assertThat(response.description()).isEqualTo(unit.getDescription());
  }

  @Test
  @DisplayName("Given a null unit when mapping to response then return null")
  void givenNullUnit_whenToResponse_thenReturnNull() {

    UnitResponse response = UnitMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
