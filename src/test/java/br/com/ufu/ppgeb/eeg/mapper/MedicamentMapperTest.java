package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.MedicamentResponse;
import br.com.ufu.ppgeb.eeg.model.Medicament;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MedicamentMapperTest {

  @Test
  @DisplayName("Given a medicament when mapping to response then map every field")
  void givenMedicament_whenToResponse_thenMapEveryField() {
    Medicament medicament = Instancio.create(Medicament.class);

    MedicamentResponse response = MedicamentMapper.toResponse(medicament);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(medicament.getId());
    assertThat(response.name()).isEqualTo(medicament.getName());
    assertThat(response.description()).isEqualTo(medicament.getDescription());
    assertThat(response.createdAt()).isEqualTo(medicament.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(medicament.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(medicament.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(medicament.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null medicament when mapping to response then return null")
  void givenNullMedicament_whenToResponse_thenReturnNull() {

    MedicamentResponse response = MedicamentMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
