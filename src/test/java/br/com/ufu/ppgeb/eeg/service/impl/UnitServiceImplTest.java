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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

  private static final int TWO_UNITS = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

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
    when(unitRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(units));

    Page<Unit> result = unitService.findAll(PAGEABLE);

    assertThat(result.getContent()).hasSize(TWO_UNITS);
    verify(unitRepository).findAll(PAGEABLE);
  }

  @Test
  @DisplayName("Given no units in database when findAll then return empty list")
  void givenNoUnitsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(unitRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(Collections.emptyList()));

    Page<Unit> result = unitService.findAll(PAGEABLE);

    assertThat(result.getContent()).isEmpty();
    verify(unitRepository).findAll(PAGEABLE);
  }
}
