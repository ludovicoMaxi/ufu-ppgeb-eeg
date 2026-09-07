package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import br.com.ufu.ppgeb.eeg.repository.EquipmentRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

  private static final String EQUIPMENT_NAME = "BRAINVISIAN";
  private static final String EQUIPMENT_NAME_LOWER = "brainvisian";
  private static final String NONEXISTENT_NAME = "NONEXISTENT";
  private static final String MSG_EQUIPMENT_NULL = "equipment cannot be null.";
  private static final String MSG_NAME_EMPTY = "equipment name cannot be empty.";
  private static final String MSG_DUPLICATED = "Equipamento já cadastrado: ";
  private static final int TWO_EQUIPMENTS = 2;
  private static final int ONE_EQUIPMENT = 1;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private EquipmentRepository equipmentRepository;

  @InjectMocks
  private EquipmentServiceImpl equipmentService;

  @Test
  @DisplayName("Given two equipments in database when findAll then return all equipments")
  void givenTwoEquipmentsInDatabase_whenFindAll_thenReturnAllEquipments() {
    Equipment equipment1 = Instancio.create(Equipment.class);
    Equipment equipment2 = Instancio.create(Equipment.class);

    List<Equipment> equipments = List.of(equipment1, equipment2);
    when(equipmentRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(equipments));

    Page<Equipment> result = equipmentService.findAll(PAGEABLE);

    assertThat(result.getContent()).hasSize(TWO_EQUIPMENTS);
    verify(equipmentRepository).findAll(PAGEABLE);
  }

  @Test
  @DisplayName("Given no equipments in database when findAll then return empty list")
  void givenNoEquipmentsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(equipmentRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(Collections.emptyList()));

    Page<Equipment> result = equipmentService.findAll(PAGEABLE);

    assertThat(result.getContent()).isEmpty();
    verify(equipmentRepository).findAll(PAGEABLE);
  }

  @Test
  @DisplayName("Given equipment name exists when findByName then return matching equipments")
  void givenEquipmentNameExists_whenFindByName_thenReturnMatchingEquipments() {
    Equipment equipment = Instancio.of(Equipment.class)
        .set(field(Equipment::getName), EQUIPMENT_NAME)
        .create();

    when(equipmentRepository.findByName(EQUIPMENT_NAME)).thenReturn(List.of(equipment));

    List<Equipment> result = equipmentService.findByName(EQUIPMENT_NAME);

    assertThat(result).hasSize(ONE_EQUIPMENT);
    assertThat(result.get(0).getName()).isEqualTo(EQUIPMENT_NAME);
    verify(equipmentRepository).findByName(EQUIPMENT_NAME);
  }

  @Test
  @DisplayName("Given equipment name does not exist when findByName then return empty list")
  void givenEquipmentNameDoesNotExist_whenFindByName_thenReturnEmptyList() {
    when(equipmentRepository.findByName(anyString())).thenReturn(Collections.emptyList());

    List<Equipment> result = equipmentService.findByName(NONEXISTENT_NAME);

    assertThat(result).isEmpty();
    verify(equipmentRepository).findByName(NONEXISTENT_NAME);
  }

  @Test
  @DisplayName("Given valid equipment when save then return saved equipment with uppercase name")
  void givenValidEquipment_whenSave_thenReturnSavedEquipmentWithUppercaseName() {
    Equipment equipment = Instancio.of(Equipment.class)
        .set(field(Equipment::getName), EQUIPMENT_NAME_LOWER)
        .create();

    when(equipmentRepository.existsByName(EQUIPMENT_NAME)).thenReturn(false);
    when(equipmentRepository.save(any(Equipment.class)))
        .thenReturn(equipment);

    Equipment result = equipmentService.save(equipment);

    assertThat(result.getName()).isEqualTo(EQUIPMENT_NAME);
    verify(equipmentRepository).existsByName(EQUIPMENT_NAME);
    verify(equipmentRepository).save(equipment);
  }

  @Test
  @DisplayName("Given null equipment when save then throw exception")
  void givenNullEquipment_whenSave_thenThrowException() {
    assertThatThrownBy(() -> equipmentService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EQUIPMENT_NULL);

    verify(equipmentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given equipment with empty name when save then throw exception")
  void givenEquipmentWithEmptyName_whenSave_thenThrowException() {
    Equipment equipment = Instancio.of(Equipment.class)
        .set(field(Equipment::getName), "")
        .create();

    assertThatThrownBy(() -> equipmentService.save(equipment))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NAME_EMPTY);

    verify(equipmentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given duplicated equipment name when save then throw exception")
  void givenDuplicatedEquipmentName_whenSave_thenThrowException() {
    Equipment equipment = Instancio.of(Equipment.class)
        .set(field(Equipment::getName), EQUIPMENT_NAME_LOWER)
        .create();

    when(equipmentRepository.existsByName(EQUIPMENT_NAME)).thenReturn(true);

    assertThatThrownBy(() -> equipmentService.save(equipment))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DUPLICATED + EQUIPMENT_NAME);

    verify(equipmentRepository, never()).save(any());
  }
}
