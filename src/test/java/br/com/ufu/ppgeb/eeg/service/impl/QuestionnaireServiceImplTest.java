package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import br.com.ufu.ppgeb.eeg.repository.QuestionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireAnswerRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireRepository;
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
class QuestionnaireServiceImplTest {

  private static final String MSG_QUESTIONNAIRE_NULL = "questionnaire cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Questionnaire";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final String MSG_QUESTION_NOT_FOUND = "Question not found: ";
  private static final String MSG_QUESTION_ID_NULL = "questionId cannot be null.";
  private static final Long QUESTIONNAIRE_ID = 1L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long QUESTION_ID = 10L;
  private static final Long OTHER_QUESTION_ID = 11L;
  private static final int TWO_QUESTIONNAIRES = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private QuestionnaireRepository questionnaireRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private QuestionnaireAnswerRepository questionnaireAnswerRepository;

  @InjectMocks
  private QuestionnaireServiceImpl questionnaireService;

  @Test
  @DisplayName("Given two questionnaires in database when findByFilter then return all questionnaires")
  void givenTwoQuestionnairesInDatabase_whenFindByFilter_thenReturnAllQuestionnaires() {
    Questionnaire questionnaire1 = Instancio.create(Questionnaire.class);
    Questionnaire questionnaire2 = Instancio.create(Questionnaire.class);

    when(questionnaireRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(questionnaire1, questionnaire2),
            PAGEABLE, TWO_QUESTIONNAIRES));

    Page<Questionnaire> result = questionnaireService.findByFilter(null, null, PAGEABLE);

    assertThat(result.getContent()).hasSize(TWO_QUESTIONNAIRES);
    verify(questionnaireRepository).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  @DisplayName("Given existing id when findById then return questionnaire")
  void givenExistingId_whenFindById_thenReturnQuestionnaire() {
    Questionnaire questionnaire = Instancio.create(Questionnaire.class);
    questionnaire.setId(QUESTIONNAIRE_ID);

    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));

    Questionnaire result = questionnaireService.findById(QUESTIONNAIRE_ID);

    assertThat(result.getId()).isEqualTo(QUESTIONNAIRE_ID);
    verify(questionnaireRepository).findById(QUESTIONNAIRE_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {

    assertThatThrownBy(() -> questionnaireService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(questionnaireRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(questionnaireRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionnaireService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid questionnaire when save then return saved questionnaire")
  void givenValidQuestionnaire_whenSave_thenReturnSavedQuestionnaire() {
    Questionnaire questionnaire = createQuestionnaire(null,
        List.of(QUESTION_ID, OTHER_QUESTION_ID));
    when(questionRepository.findAllById(List.of(QUESTION_ID, OTHER_QUESTION_ID)))
        .thenReturn(existingQuestions(List.of(QUESTION_ID, OTHER_QUESTION_ID)));
    when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(questionnaire);

    questionnaireService.save(questionnaire);

    verify(questionnaireRepository).save(questionnaire);
    verify(questionRepository).findAllById(List.of(QUESTION_ID, OTHER_QUESTION_ID));
  }

  @Test
  @DisplayName("Given questionnaire without questions when save then return saved questionnaire")
  void givenQuestionnaireWithoutQuestions_whenSave_thenReturnSavedQuestionnaire() {
    Questionnaire questionnaire = createQuestionnaire(null, List.of());
    when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(questionnaire);

    questionnaireService.save(questionnaire);

    verify(questionnaireRepository).save(questionnaire);
  }

  @Test
  @DisplayName("Given questionnaire with non-null id when save then throw exception")
  void givenQuestionnaireWithNonNullId_whenSave_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID, List.of());

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("id must be null");

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null questionnaire when save then throw exception")
  void givenNullQuestionnaire_whenSave_thenThrowException() {

    assertThatThrownBy(() -> questionnaireService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTIONNAIRE_NULL);

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given questionnaire with blank title when save then throw exception")
  void givenQuestionnaireWithBlankTitle_whenSave_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(null, List.of());
    questionnaire.setTitle("");

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("title cannot be empty.");

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given questionnaire with nonexistent question when save then throw exception")
  void givenQuestionnaireWithNonexistentQuestion_whenSave_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(null, List.of(QUESTION_ID));
    when(questionRepository.findAllById(List.of(QUESTION_ID))).thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTION_NOT_FOUND + QUESTION_ID);

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given questionnaire with two nonexistent questions when save then list all of them")
  void givenQuestionnaireWithTwoNonexistentQuestions_whenSave_thenListAllOfThem() {
    Questionnaire questionnaire = createQuestionnaire(null,
        List.of(QUESTION_ID, OTHER_QUESTION_ID));
    when(questionRepository.findAllById(List.of(QUESTION_ID, OTHER_QUESTION_ID)))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTION_NOT_FOUND + QUESTION_ID + ", " + OTHER_QUESTION_ID);

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given questionnaire with questions without id when save then throw exception")
  void givenQuestionnaireWithQuestionsWithoutId_whenSave_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(null, Arrays.asList(QUESTION_ID, null, null));

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTION_ID_NULL);

    verify(questionRepository, never()).findAllById(any());
    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given questionnaire with duplicate questions when save then throw exception")
  void givenQuestionnaireWithDuplicateQuestions_whenSave_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(null,
        List.of(QUESTION_ID, QUESTION_ID));

    assertThatThrownBy(() -> questionnaireService.save(questionnaire))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("The questionnaire cannot have duplicate questions.");

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given valid questionnaire when update then return updated questionnaire")
  void givenValidQuestionnaire_whenUpdate_thenReturnUpdatedQuestionnaire() {
    final Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID,
        List.of(QUESTION_ID));
    Questionnaire old = createQuestionnaire(QUESTIONNAIRE_ID, List.of(QUESTION_ID));
    when(questionRepository.findAllById(List.of(QUESTION_ID)))
        .thenReturn(existingQuestions(List.of(QUESTION_ID)));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.of(old));
    when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(old);

    questionnaireService.update(questionnaire);

    assertThat(old.getTitle()).isEqualTo(questionnaire.getTitle());
    assertThat(old.getDescription()).isEqualTo(questionnaire.getDescription());
  }

  @Test
  @DisplayName("Given existing question link when update with new sort order then keep single link")
  void givenExistingQuestionLink_whenUpdateWithNewSortOrder_thenKeepSingleLink() {
    final Long newSortOrder = 3L;
    Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID,
        List.of(QUESTION_ID));
    questionnaire.getQuestionnaireQuestions().get(0).setSortOrder(newSortOrder);
    Questionnaire old = createQuestionnaire(QUESTIONNAIRE_ID, List.of(QUESTION_ID));
    when(questionRepository.findAllById(List.of(QUESTION_ID)))
        .thenReturn(existingQuestions(List.of(QUESTION_ID)));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.of(old));
    when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(old);

    questionnaireService.update(questionnaire);

    List<QuestionnaireQuestion> links = old.getQuestionnaireQuestions();
    assertThat(links).hasSize(1);
    assertThat(links.get(0).getQuestion().getId()).isEqualTo(QUESTION_ID);
    assertThat(links.get(0).getSortOrder()).isEqualTo(newSortOrder);
  }

  @Test
  @DisplayName("Given existing question link when update without it then remove link")
  void givenExistingQuestionLink_whenUpdateWithoutIt_thenRemoveLink() {
    final Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID, List.of());
    Questionnaire old = createQuestionnaire(QUESTIONNAIRE_ID, List.of(QUESTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.of(old));
    when(questionnaireRepository.save(any(Questionnaire.class))).thenReturn(old);

    questionnaireService.update(questionnaire);

    assertThat(old.getQuestionnaireQuestions()).isEmpty();
  }

  @Test
  @DisplayName("Given nonexistent questionnaire when update then throw exception")
  void givenNonexistentQuestionnaire_whenUpdate_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(NONEXISTENT_ID, List.of());
    when(questionnaireRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionnaireService.update(questionnaire))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(questionnaireRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove questionnaire")
  void givenExistingId_whenDelete_thenRemoveQuestionnaire() {
    Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID, List.of());
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository.existsByQuestionnaire(questionnaire)).thenReturn(false);

    questionnaireService.delete(QUESTIONNAIRE_ID);

    verify(questionnaireRepository).delete(questionnaire);
  }

  @Test
  @DisplayName("Given questionnaire with answers when delete then throw exception")
  void givenQuestionnaireWithAnswers_whenDelete_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(QUESTIONNAIRE_ID, List.of());
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository.existsByQuestionnaire(questionnaire)).thenReturn(true);

    assertThatThrownBy(() -> questionnaireService.delete(QUESTIONNAIRE_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Questionnaire has answers and cannot be removed.");

    verify(questionnaireRepository, never()).delete(any(Questionnaire.class));
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {

    assertThatThrownBy(() -> questionnaireService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(questionnaireRepository, never()).deleteById(any());
  }

  private Questionnaire createQuestionnaire(Long id, List<Long> questionIds) {

    List<QuestionnaireQuestion> links = questionIds.stream()
        .map(questionId -> QuestionnaireQuestion.builder()
            .question(Question.builder().id(questionId).build())
            .sortOrder(1L)
            .build())
        .collect(Collectors.toCollection(ArrayList::new));
    Questionnaire questionnaire = Instancio.create(Questionnaire.class);
    questionnaire.setId(id);
    questionnaire.setQuestionnaireQuestions(links);
    return questionnaire;
  }

  private List<Question> existingQuestions(List<Long> questionIds) {

    return questionIds.stream()
        .map(questionId -> Question.builder().id(questionId).build())
        .toList();
  }
}