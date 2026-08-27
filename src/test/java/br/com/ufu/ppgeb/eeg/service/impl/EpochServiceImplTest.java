package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.repository.EpochRepository;
import br.com.ufu.ppgeb.eeg.view.EpochList;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpochServiceImplTest {

  private static final String MSG_EPOCH_NULL = "epoch cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_START_TIME_NULL = "start time cannot be null.";
  private static final String MSG_DURATION_NULL = "duration cannot be null.";
  private static final String MSG_DESCRIPTION_EMPTY = "description cannot be empty.";
  private static final String MSG_EXAM_ID_NULL = "examId cannot be null.";
  private static final String MSG_EPOCH_LIST_NULL = "EpochList cannot be null.";
  private static final String RESOURCE_NAME = "Epoch";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final Long EPOCH_ID = 1L;
  private static final Long EXAM_ID = 10L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long DIFFERENT_EXAM_ID = 999L;
  private static final int TWO_EPOCHS = 2;

  @Mock
  private EpochRepository epochRepository;

  @InjectMocks
  private EpochServiceImpl epochService;

  @Test
  @DisplayName("Given two epochs in database when findAll then return all epochs")
  void givenTwoEpochsInDatabase_whenFindAll_thenReturnAllEpochs() {
    Epoch epoch1 = Instancio.create(Epoch.class);
    Epoch epoch2 = Instancio.create(Epoch.class);

    List<Epoch> epochs = List.of(epoch1, epoch2);
    when(epochRepository.findAll()).thenReturn(epochs);

    List<Epoch> result = epochService.findAll();

    assertThat(result).hasSize(TWO_EPOCHS);
    verify(epochRepository).findAll();
  }

  @Test
  @DisplayName("Given no epochs in database when findAll then return empty list")
  void givenNoEpochsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(epochRepository.findAll()).thenReturn(Collections.emptyList());

    List<Epoch> result = epochService.findAll();

    assertThat(result).isEmpty();
    verify(epochRepository).findAll();
  }

  @Test
  @DisplayName("Given valid epoch when save then return saved epoch")
  void givenValidEpoch_whenSave_thenReturnSavedEpoch() {
    Epoch epoch = Instancio.create(Epoch.class);

    when(epochRepository.save(any(Epoch.class)))
        .thenReturn(epoch);

    epochService.save(epoch);

    verify(epochRepository).save(epoch);
  }

  @Test
  @DisplayName("Given null epoch when save then throw exception")
  void givenNullEpoch_whenSave_thenThrowException() {
    assertThatThrownBy(() -> epochService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EPOCH_NULL);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch with null start time when save then throw exception")
  void givenEpochWithNullStartTime_whenSave_thenThrowException() {
    Epoch epoch = Instancio.create(Epoch.class);
    epoch.setStartTime(null);

    assertThatThrownBy(() -> epochService.save(epoch))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_START_TIME_NULL);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch with null duration when save then throw exception")
  void givenEpochWithNullDuration_whenSave_thenThrowException() {
    Epoch epoch = Instancio.create(Epoch.class);
    epoch.setDuration(null);

    assertThatThrownBy(() -> epochService.save(epoch))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DURATION_NULL);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch with empty description when save then throw exception")
  void givenEpochWithEmptyDescription_whenSave_thenThrowException() {
    Epoch epoch = Instancio.create(Epoch.class);
    epoch.setDescription("");

    assertThatThrownBy(() -> epochService.save(epoch))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_DESCRIPTION_EMPTY);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return epoch")
  void givenExistingId_whenFindById_thenReturnEpoch() {
    Epoch epoch = Instancio.create(Epoch.class);
    epoch.setId(EPOCH_ID);

    when(epochRepository.findById(EPOCH_ID)).thenReturn(Optional.of(epoch));

    Epoch result = epochService.findById(EPOCH_ID);

    assertThat(result.getId()).isEqualTo(EPOCH_ID);
    verify(epochRepository).findById(EPOCH_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> epochService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(epochRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(epochRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> epochService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid exam id when findByFilter then return matching epochs")
  void givenValidExamId_whenFindByFilter_thenReturnMatchingEpochs() {
    Epoch epoch = Instancio.create(Epoch.class);

    when(epochRepository.findByExamId(EXAM_ID)).thenReturn(List.of(epoch));

    List<Epoch> result = epochService.findByFilter(EXAM_ID);

    assertThat(result).hasSize(1);
    verify(epochRepository).findByExamId(EXAM_ID);
  }

  @Test
  @DisplayName("Given null exam id when findByFilter then throw exception")
  void givenNullExamId_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> epochService.findByFilter(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EXAM_ID_NULL);

    verify(epochRepository, never()).findByExamId(any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove epoch")
  void givenExistingId_whenDelete_thenRemoveEpoch() {
    when(epochRepository.existsById(EPOCH_ID)).thenReturn(true);

    epochService.delete(EPOCH_ID);

    verify(epochRepository).existsById(EPOCH_ID);
    verify(epochRepository).deleteById(EPOCH_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> epochService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(epochRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(epochRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> epochService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(epochRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given null epoch list when updateList then throw exception")
  void givenNullEpochList_whenUpdateList_thenThrowException() {
    assertThatThrownBy(() -> epochService.updateList(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_EPOCH_LIST_NULL);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch list with null exam id when updateList then throw exception")
  void givenEpochListWithNullExamId_whenUpdateList_thenThrowException() {
    EpochList epochList = new EpochList();
    epochList.setExamId(null);

    assertThatThrownBy(() -> epochService.updateList(epochList))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("ExamId cannot be null.");

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch list with new epochs when updateList then return saved epochs")
  void givenEpochListWithNewEpochs_whenUpdateList_thenReturnSavedEpochs() {
    Epoch epoch = createEpoch(null, EXAM_ID);
    EpochList epochList = createEpochList(EXAM_ID, epoch);

    when(epochRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());
    when(epochRepository.save(any(Epoch.class)))
        .thenReturn(epoch);

    List<Epoch> result = epochService.updateList(epochList);

    assertThat(result).hasSize(1);
    verify(epochRepository).findByExamId(EXAM_ID);
    verify(epochRepository).save(epoch);
  }

  private Epoch createEpoch(Long id, Long examId) {
    Epoch epoch = Instancio.create(Epoch.class);
    epoch.setId(id);
    epoch.setExamId(examId);
    return epoch;
  }

  private EpochList createEpochList(Long examId, Epoch epoch) {
    EpochList epochList = new EpochList();
    epochList.setExamId(examId);
    epochList.setEpochs(List.of(epoch));
    return epochList;
  }

  @Test
  @DisplayName("Given epoch with different exam id in list when updateList then throw exception")
  void givenEpochWithDifferentExamIdInList_whenUpdateList_thenThrowException() {
    Epoch epoch = createEpoch(null, DIFFERENT_EXAM_ID);
    EpochList epochList = createEpochList(EXAM_ID, epoch);

    when(epochRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> epochService.updateList(epochList))
        .isInstanceOf(IllegalArgumentException.class);

    verify(epochRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given epoch with nonexistent id in list when updateList then throw exception")
  void givenEpochWithNonexistentIdInList_whenUpdateList_thenThrowException() {
    Epoch epoch = createEpoch(NONEXISTENT_ID, EXAM_ID);
    EpochList epochList = createEpochList(EXAM_ID, epoch);

    when(epochRepository.findByExamId(EXAM_ID)).thenReturn(Collections.emptyList());

    assertThatThrownBy(() -> epochService.updateList(epochList))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Epoch with id=" + NONEXISTENT_ID + " not exist by examID=" + EXAM_ID);

    verify(epochRepository, never()).save(any());
  }
}
