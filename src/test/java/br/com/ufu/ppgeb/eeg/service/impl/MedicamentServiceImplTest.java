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

import br.com.ufu.ppgeb.eeg.model.Medicament;
import br.com.ufu.ppgeb.eeg.repository.MedicamentRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MedicamentServiceImplTest {

  private static final String MEDICAMENT_NAME = "DIPIRONA";
  private static final String MEDICAMENT_NAME_LOWER = "dipirona";
  private static final String NONEXISTENT_NAME = "NONEXISTENT";
  private static final String MSG_MEDICAMENT_NULL = "medicament cannot be null.";
  private static final String MSG_NAME_EMPTY = "medicament name cannot be empty.";
  private static final String MSG_DUPLICATED = "Medicamento já cadastrado: ";
  private static final int TWO_MEDICAMENTS = 2;
  private static final int ONE_MEDICAMENT = 1;

  @Mock
  private MedicamentRepository medicamentRepository;

  @InjectMocks
  private MedicamentServiceImpl medicamentService;

  @Test
  @DisplayName("Given two medicaments in database when findAll then return all medicaments")
  void givenTwoMedicamentsInDatabase_whenFindAll_thenReturnAllMedicaments() {
    Medicament medicament1 = Instancio.create(Medicament.class);
    Medicament medicament2 = Instancio.create(Medicament.class);

    List<Medicament> medicaments = List.of(medicament1, medicament2);
    when(medicamentRepository.findAll()).thenReturn(medicaments);

    List<Medicament> result = medicamentService.findAll();

    assertThat(result).hasSize(TWO_MEDICAMENTS);
    verify(medicamentRepository).findAll();
  }

  @Test
  @DisplayName("Given no medicaments in database when findAll then return empty list")
  void givenNoMedicamentsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(medicamentRepository.findAll()).thenReturn(Collections.emptyList());

    List<Medicament> result = medicamentService.findAll();

    assertThat(result).isEmpty();
    verify(medicamentRepository).findAll();
  }

  @Test
  @DisplayName("Given medicament name exists when findByName then return matching medicaments")
  void givenMedicamentNameExists_whenFindByName_thenReturnMatchingMedicaments() {
    Medicament medicament = Instancio.of(Medicament.class)
        .set(field(Medicament::getName), MEDICAMENT_NAME)
        .create();

    when(medicamentRepository.findByName(MEDICAMENT_NAME)).thenReturn(List.of(medicament));

    List<Medicament> result = medicamentService.findByName(MEDICAMENT_NAME);

    assertThat(result).hasSize(ONE_MEDICAMENT);
    assertThat(result.get(0).getName()).isEqualTo(MEDICAMENT_NAME);
    verify(medicamentRepository).findByName(MEDICAMENT_NAME);
  }

  @Test
  @DisplayName("Given medicament name does not exist when findByName then return empty list")
  void givenMedicamentNameDoesNotExist_whenFindByName_thenReturnEmptyList() {
    when(medicamentRepository.findByName(anyString())).thenReturn(Collections.emptyList());

    List<Medicament> result = medicamentService.findByName(NONEXISTENT_NAME);

    assertThat(result).isEmpty();
    verify(medicamentRepository).findByName(NONEXISTENT_NAME);
  }

  @Test
  @DisplayName("Given valid medicament when save then return saved medicament with uppercase name")
  void givenValidMedicament_whenSave_thenReturnSavedMedicamentWithUppercaseName() {
    Medicament medicament = Instancio.of(Medicament.class)
        .set(field(Medicament::getName), MEDICAMENT_NAME_LOWER)
        .create();

    when(medicamentRepository.existsByName(MEDICAMENT_NAME)).thenReturn(false);
    when(medicamentRepository.save(any(Medicament.class)))
        .thenReturn(medicament);

    Medicament result = medicamentService.save(medicament);

    assertThat(result.getName()).isEqualTo(MEDICAMENT_NAME);
    verify(medicamentRepository).existsByName(MEDICAMENT_NAME);
    verify(medicamentRepository).save(medicament);
  }

  @Test
  @DisplayName("Given null medicament when save then throw exception")
  void givenNullMedicament_whenSave_thenThrowException() {
    assertThatThrownBy(() -> medicamentService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_MEDICAMENT_NULL);

    verify(medicamentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given medicament with empty name when save then throw exception")
  void givenMedicamentWithEmptyName_whenSave_thenThrowException() {
    Medicament medicament = Instancio.of(Medicament.class)
        .set(field(Medicament::getName), "")
        .create();

    assertThatThrownBy(() -> medicamentService.save(medicament))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NAME_EMPTY);

    verify(medicamentRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given duplicated medicament name when save then throw exception")
  void givenDuplicatedMedicamentName_whenSave_thenThrowException() {
    Medicament medicament = Instancio.of(Medicament.class)
        .set(field(Medicament::getName), MEDICAMENT_NAME_LOWER)
        .create();

    when(medicamentRepository.existsByName(MEDICAMENT_NAME)).thenReturn(true);

    assertThatThrownBy(() -> medicamentService.save(medicament))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DUPLICATED + MEDICAMENT_NAME);

    verify(medicamentRepository, never()).save(any());
  }
}
