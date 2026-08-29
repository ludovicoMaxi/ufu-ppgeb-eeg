package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.EquipmentResponse;
import br.com.ufu.ppgeb.eeg.model.Equipment;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EquipmentMapperTest {

  @Test
  @DisplayName("Given an equipment when mapping to response then map every field")
  void givenEquipment_whenToResponse_thenMapEveryField() {
    Equipment equipment = Instancio.create(Equipment.class);

    EquipmentResponse response = EquipmentMapper.toResponse(equipment);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(equipment.getId());
    assertThat(response.name()).isEqualTo(equipment.getName());
    assertThat(response.description()).isEqualTo(equipment.getDescription());
    assertThat(response.createdAt()).isEqualTo(equipment.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(equipment.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(equipment.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(equipment.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null equipment when mapping to response then return null")
  void givenNullEquipment_whenToResponse_thenReturnNull() {

    EquipmentResponse response = EquipmentMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
