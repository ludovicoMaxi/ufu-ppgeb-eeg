package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.ProfessionResponse;
import br.com.ufu.ppgeb.eeg.model.Profession;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProfessionMapperTest {

  @Test
  @DisplayName("Given a profession when mapping to response then map every field")
  void givenProfession_whenToResponse_thenMapEveryField() {
    Profession profession = Instancio.create(Profession.class);

    ProfessionResponse response = ProfessionMapper.toResponse(profession);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(profession.getId());
    assertThat(response.name()).isEqualTo(profession.getName());
  }

  @Test
  @DisplayName("Given a null profession when mapping to response then return null")
  void givenNullProfession_whenToResponse_thenReturnNull() {

    ProfessionResponse response = ProfessionMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
