package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.repository.AnswerOptionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionAnswerOptionRepository;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class AnswerOptionServiceImplTest {

  private static final String MSG_NULL = "answerOption cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "AnswerOption";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final Long ANSWER_OPTION_ID = 1L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final int TWO_OPTIONS = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private AnswerOptionRepository answerOptionRepository;

  @Mock
  private QuestionAnswerOptionRepository questionAnswerOptionRepository;

  @InjectMocks
  private AnswerOptionServiceImpl answerOptionService;

  @Test
  @DisplayName("Given two answer options in database when findByFilter then return all answer options")
  void givenTwoAnswerOptionsInDatabase_whenFindByFilter_thenReturnAllAnswerOptions() {
    AnswerOption answerOption1 = Instancio.create(AnswerOption.class);
    AnswerOption answerOption2 = Instancio.create(AnswerOption.class);

    when(answerOptionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(answerOption1, answerOption2), PAGEABLE, TWO_OPTIONS));

    Page<AnswerOption> result = answerOptionService.findByFilter(null, null, null, PAGEABLE);

    assertThat(result.getContent()).hasSize(TWO_OPTIONS);
    verify(answerOptionRepository).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  @DisplayName("Given existing id when findById then return answer option")
  void givenExistingId_whenFindById_thenReturnAnswerOption() {
    AnswerOption answerOption = Instancio.create(AnswerOption.class);
    answerOption.setId(ANSWER_OPTION_ID);

    when(answerOptionRepository.findById(ANSWER_OPTION_ID)).thenReturn(Optional.of(answerOption));

    AnswerOption result = answerOptionService.findById(ANSWER_OPTION_ID);

    assertThat(result.getId()).isEqualTo(ANSWER_OPTION_ID);
    verify(answerOptionRepository).findById(ANSWER_OPTION_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {

    assertThatThrownBy(() -> answerOptionService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(answerOptionRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(answerOptionRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> answerOptionService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid answer option when save then return saved answer option")
  void givenValidAnswerOption_whenSave_thenReturnSavedAnswerOption() {
    AnswerOption answerOption = createAnswerOption(null);
    when(answerOptionRepository.save(any(AnswerOption.class))).thenReturn(answerOption);

    answerOptionService.save(answerOption);

    verify(answerOptionRepository).save(answerOption);
  }

  @Test
  @DisplayName("Given answer option with non-null id when save then throw exception")
  void givenAnswerOptionWithNonNullId_whenSave_thenThrowException() {
    AnswerOption answerOption = createAnswerOption(ANSWER_OPTION_ID);

    assertThatThrownBy(() -> answerOptionService.save(answerOption))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("id must be null");

    verify(answerOptionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null answer option when save then throw exception")
  void givenNullAnswerOption_whenSave_thenThrowException() {

    assertThatThrownBy(() -> answerOptionService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NULL);

    verify(answerOptionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given answer option with blank text when save then throw exception")
  void givenAnswerOptionWithBlankText_whenSave_thenThrowException() {
    AnswerOption answerOption = createAnswerOption(null);
    answerOption.setText("");

    assertThatThrownBy(() -> answerOptionService.save(answerOption))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("text cannot be empty.");

    verify(answerOptionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given valid answer option when update then return updated answer option")
  void givenValidAnswerOption_whenUpdate_thenReturnUpdatedAnswerOption() {
    AnswerOption answerOption = createAnswerOption(ANSWER_OPTION_ID);
    AnswerOption old = createAnswerOption(ANSWER_OPTION_ID);

    when(answerOptionRepository.findById(ANSWER_OPTION_ID)).thenReturn(Optional.of(old));
    when(answerOptionRepository.save(any(AnswerOption.class))).thenReturn(old);

    answerOptionService.update(answerOption);

    assertThat(old.getText()).isEqualTo(answerOption.getText());
    assertThat(old.getActive()).isEqualTo(answerOption.getActive());
    verify(answerOptionRepository).save(old);
  }

  @Test
  @DisplayName("Given existing id when delete then remove answer option")
  void givenExistingId_whenDelete_thenRemoveAnswerOption() {
    AnswerOption answerOption = createAnswerOption(ANSWER_OPTION_ID);
    when(answerOptionRepository.findById(ANSWER_OPTION_ID)).thenReturn(Optional.of(answerOption));
    when(questionAnswerOptionRepository.existsByAnswerOption(answerOption)).thenReturn(false);

    answerOptionService.delete(ANSWER_OPTION_ID);

    verify(answerOptionRepository).delete(answerOption);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {

    assertThatThrownBy(() -> answerOptionService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(answerOptionRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given answer option referenced by a question when delete then throw exception")
  void givenAnswerOptionReferencedByQuestion_whenDelete_thenThrowException() {
    AnswerOption answerOption = createAnswerOption(ANSWER_OPTION_ID);
    when(answerOptionRepository.findById(ANSWER_OPTION_ID)).thenReturn(Optional.of(answerOption));
    when(questionAnswerOptionRepository.existsByAnswerOption(answerOption)).thenReturn(true);

    assertThatThrownBy(() -> answerOptionService.delete(ANSWER_OPTION_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Answer option is referenced by questions and cannot be removed.");

    verify(answerOptionRepository, never()).delete(any(AnswerOption.class));
  }

  private AnswerOption createAnswerOption(Long id) {
    AnswerOption answerOption = Instancio.create(AnswerOption.class);
    answerOption.setId(id);
    return answerOption;
  }
}