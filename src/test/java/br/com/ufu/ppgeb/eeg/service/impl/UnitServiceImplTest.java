package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Unit;
import br.com.ufu.ppgeb.eeg.repository.UnitRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

  private static final int TWO_UNITS = 2;

  @Mock
  private UnitRepository unitRepository;

  @InjectMocks
  private UnitServiceImpl unitService;

  @Test
  @DisplayName("Given two units in database when findAll then return all units")
  void givenTwoUnitsInDatabase_whenFindAll_thenReturnAllUnits() {
    Unit unit1 = Instancio.create(Unit.class);
    Unit unit2 = Instancio.create(Unit.class);

    List<Unit> units = List.of(unit1, unit2);
    when(unitRepository.findAll()).thenReturn(units);

    List<Unit> result = unitService.findAll();

    assertThat(result).hasSize(TWO_UNITS);
    verify(unitRepository).findAll();
  }

  @Test
  @DisplayName("Given no units in database when findAll then return empty list")
  void givenNoUnitsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(unitRepository.findAll()).thenReturn(Collections.emptyList());

    List<Unit> result = unitService.findAll();

    assertThat(result).isEmpty();
    verify(unitRepository).findAll();
  }
}
