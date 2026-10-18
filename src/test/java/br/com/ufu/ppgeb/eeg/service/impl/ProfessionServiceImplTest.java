package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.model.Profession;
import br.com.ufu.ppgeb.eeg.repository.ProfessionRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProfessionServiceImplTest {

  private static final String RAW_NAME = "  analista de ti ";
  private static final String NORMALIZED_NAME = "ANALISTA DE TI";

  @Mock
  private ProfessionRepository professionRepository;

  @InjectMocks
  private ProfessionServiceImpl professionService;

  @Test
  @DisplayName("Given professions in database when findAll then return them ordered by name")
  void givenProfessionsInDatabase_whenFindAll_thenReturnOrderedByName() {
    List<Profession> professions = List.of(Instancio.create(Profession.class));
    when(professionRepository.findAllByOrderByNameAsc()).thenReturn(professions);

    List<Profession> result = professionService.findAll();

    assertThat(result).hasSize(1);
    verify(professionRepository).findAllByOrderByNameAsc();
  }

  @Test
  @DisplayName("Given new job name when registerIfAbsent then save normalized profession")
  void givenNewJobName_whenRegisterIfAbsent_thenSaveNormalizedProfession() {
    when(professionRepository.findByName(NORMALIZED_NAME)).thenReturn(Optional.empty());

    professionService.registerIfAbsent(RAW_NAME);

    ArgumentCaptor<Profession> captor = ArgumentCaptor.forClass(Profession.class);
    verify(professionRepository).save(captor.capture());
    assertThat(captor.getValue().getName()).isEqualTo(NORMALIZED_NAME);
  }

  @Test
  @DisplayName("Given existing job name when registerIfAbsent then do not save again")
  void givenExistingJobName_whenRegisterIfAbsent_thenNotSaveAgain() {
    when(professionRepository.findByName(NORMALIZED_NAME))
        .thenReturn(Optional.of(Instancio.create(Profession.class)));

    professionService.registerIfAbsent(RAW_NAME);

    verify(professionRepository, never()).save(any(Profession.class));
  }

  @Test
  @DisplayName("Given blank job name when registerIfAbsent then ignore it")
  void givenBlankJobName_whenRegisterIfAbsent_thenIgnoreIt() {

    professionService.registerIfAbsent("   ");
    professionService.registerIfAbsent(null);

    verify(professionRepository, never()).findByName(any());
    verify(professionRepository, never()).save(any(Profession.class));
  }
}
