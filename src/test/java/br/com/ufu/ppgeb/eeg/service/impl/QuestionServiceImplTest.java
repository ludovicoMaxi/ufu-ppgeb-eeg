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
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.repository.AnswerOptionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireQuestionRepository;
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
class QuestionServiceImplTest {

  private static final String MSG_QUESTION_NULL = "question cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Question";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final Long QUESTION_ID = 1L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long ANSWER_OPTION_ID = 10L;
  private static final Long OTHER_ANSWER_OPTION_ID = 11L;
  private static final String MSG_ANSWER_OPTION_NOT_FOUND = "Answer option not found: ";
  private static final String MSG_ANSWER_OPTION_ID_NULL = "answerOptionId cannot be null.";
  private static final int TWO_QUESTIONS = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private AnswerOptionRepository answerOptionRepository;

  @Mock
  private QuestionnaireQuestionRepository questionnaireQuestionRepository;

  @InjectMocks
  private QuestionServiceImpl questionService;

  @Test
  @DisplayName("Given two questions in database when findByFilter then return all questions")
  void givenTwoQuestionsInDatabase_whenFindByFilter_thenReturnAllQuestions() {
    Question question1 = Instancio.create(Question.class);
    Question question2 = Instancio.create(Question.class);

    when(questionRepository.findAll(any(Specification.class), any(Pageable.class)))
        .thenReturn(new PageImpl<>(List.of(question1, question2), PAGEABLE, TWO_QUESTIONS));

    Page<Question> result = questionService.findByFilter(null, null, null, null, PAGEABLE);

    assertThat(result.getContent()).hasSize(TWO_QUESTIONS);
    verify(questionRepository).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  @DisplayName("Given existing id when findById then return question")
  void givenExistingId_whenFindById_thenReturnQuestion() {
    Question question = Instancio.create(Question.class);
    question.setId(QUESTION_ID);

    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));

    Question result = questionService.findById(QUESTION_ID);

    assertThat(result.getId()).isEqualTo(QUESTION_ID);
    verify(questionRepository).findById(QUESTION_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {

    assertThatThrownBy(() -> questionService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(questionRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(questionRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid selection question when save then return saved question")
  void givenValidSelectionQuestion_whenSave_thenReturnSavedQuestion() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)))
        .thenReturn(existingAnswerOptions(List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)));
    when(questionRepository.save(any(Question.class))).thenReturn(question);

    questionService.save(question);

    verify(questionRepository).save(question);
    verify(answerOptionRepository)
        .findAllById(List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
  }

  @Test
  @DisplayName("Given valid free text question when save then return saved question")
  void givenValidFreeTextQuestion_whenSave_thenReturnSavedQuestion() {
    Question question = createQuestion(null, QuestionType.FREE_TEXT, List.of());
    when(questionRepository.save(any(Question.class))).thenReturn(question);

    questionService.save(question);

    verify(questionRepository).save(question);
  }

  @Test
  @DisplayName("Given question with non-null id when save then throw exception")
  void givenQuestionWithNonNullId_whenSave_thenThrowException() {
    Question question = createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of());

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("id must be null");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null question when save then throw exception")
  void givenNullQuestion_whenSave_thenThrowException() {

    assertThatThrownBy(() -> questionService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTION_NULL);

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with blank text when save then throw exception")
  void givenQuestionWithBlankText_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.FREE_TEXT, List.of());
    question.setText("");

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("text cannot be empty.");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with null question type when save then throw exception")
  void givenQuestionWithNullQuestionType_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.FREE_TEXT, List.of());
    question.setQuestionType(null);

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("questionType cannot be null.");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given free text question with options when save then throw exception")
  void givenFreeTextQuestionWithOptions_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.FREE_TEXT,
        List.of(ANSWER_OPTION_ID));

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("FREE_TEXT question cannot have options.");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given selection question without options when save then throw exception")
  void givenSelectionQuestionWithoutOptions_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT, List.of());

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("selection question must have options.");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with nonexistent option when save then throw exception")
  void givenQuestionWithNonexistentOption_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID))).thenReturn(List.of());

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ANSWER_OPTION_NOT_FOUND + ANSWER_OPTION_ID);

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with two nonexistent options when save then list all of them")
  void givenQuestionWithTwoNonexistentOptions_whenSave_thenListAllOfThem() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ANSWER_OPTION_NOT_FOUND + ANSWER_OPTION_ID
            + ", " + OTHER_ANSWER_OPTION_ID);

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with options without id when save then throw exception")
  void givenQuestionWithOptionsWithoutId_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT,
        Arrays.asList(ANSWER_OPTION_ID, null, null));

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ANSWER_OPTION_ID_NULL);

    verify(answerOptionRepository, never()).findAllById(any());
    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given question with duplicate options when save then throw exception")
  void givenQuestionWithDuplicateOptions_whenSave_thenThrowException() {
    Question question = createQuestion(null, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID, ANSWER_OPTION_ID));

    assertThatThrownBy(() -> questionService.save(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("The question cannot have duplicate options.");

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given valid question when update then return updated question")
  void givenValidQuestion_whenUpdate_thenReturnUpdatedQuestion() {
    final Question question = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    Question old = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID)))
        .thenReturn(existingAnswerOptions(List.of(ANSWER_OPTION_ID)));
    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(old));
    when(questionRepository.save(any(Question.class))).thenReturn(old);

    questionService.update(question);

    assertThat(old.getText()).isEqualTo(question.getText());
    assertThat(old.getQuestionType()).isEqualTo(question.getQuestionType());
  }

  @Test
  @DisplayName("Given existing option link when update with new sort order then keep single link")
  void givenExistingOptionLink_whenUpdateWithNewSortOrder_thenKeepSingleLink() {
    final Long newSortOrder = 3L;
    Question question = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    question.getQuestionAnswerOptions().get(0).setSortOrder(newSortOrder);
    Question old = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID)))
        .thenReturn(existingAnswerOptions(List.of(ANSWER_OPTION_ID)));
    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(old));
    when(questionRepository.save(any(Question.class))).thenReturn(old);

    questionService.update(question);

    List<QuestionAnswerOption> links = old.getQuestionAnswerOptions();
    assertThat(links).hasSize(1);
    assertThat(links.get(0).getAnswerOption().getId()).isEqualTo(ANSWER_OPTION_ID);
    assertThat(links.get(0).getSortOrder()).isEqualTo(newSortOrder);
  }

  @Test
  @DisplayName("Given existing option link when update without it then remove link")
  void givenExistingOptionLink_whenUpdateWithoutIt_thenRemoveLink() {
    final Question question = createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of());
    Question old = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(old));
    when(questionRepository.save(any(Question.class))).thenReturn(old);

    questionService.update(question);

    assertThat(old.getQuestionAnswerOptions()).isEmpty();
  }

  @Test
  @DisplayName("Given question with nonexistent option when update then throw exception")
  void givenQuestionWithNonexistentOption_whenUpdate_thenThrowException() {
    final Question question = createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT,
        List.of(ANSWER_OPTION_ID));
    when(answerOptionRepository.findAllById(List.of(ANSWER_OPTION_ID))).thenReturn(List.of());

    assertThatThrownBy(() -> questionService.update(question))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ANSWER_OPTION_NOT_FOUND + ANSWER_OPTION_ID);

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given nonexistent question when update then throw exception")
  void givenNonexistentQuestion_whenUpdate_thenThrowException() {
    Question question = createQuestion(NONEXISTENT_ID, QuestionType.FREE_TEXT, List.of());
    when(questionRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionService.update(question))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(questionRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when delete then remove question")
  void givenExistingId_whenDelete_thenRemoveQuestion() {
    Question question = createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of());
    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));
    when(questionnaireQuestionRepository.existsByQuestion(question)).thenReturn(false);

    questionService.delete(QUESTION_ID);

    verify(questionRepository).delete(question);
  }

  @Test
  @DisplayName("Given question referenced by a questionnaire when delete then throw exception")
  void givenQuestionReferencedByQuestionnaire_whenDelete_thenThrowException() {
    Question question = createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of());
    when(questionRepository.findById(QUESTION_ID)).thenReturn(Optional.of(question));
    when(questionnaireQuestionRepository.existsByQuestion(question)).thenReturn(true);

    assertThatThrownBy(() -> questionService.delete(QUESTION_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Question is referenced by questionnaires and cannot be removed.");

    verify(questionRepository, never()).delete(any(Question.class));
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {

    assertThatThrownBy(() -> questionService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(questionRepository, never()).deleteById(any());
  }

  private Question createQuestion(Long id, QuestionType questionType,
      List<Long> answerOptionIds) {

    List<QuestionAnswerOption> options = answerOptionIds.stream()
        .map(optionId -> QuestionAnswerOption.builder()
            .answerOption(AnswerOption.builder().id(optionId).build())
            .sortOrder(1L)
            .build())
        .collect(Collectors.toCollection(ArrayList::new));
    Question question = Instancio.create(Question.class);
    question.setId(id);
    question.setQuestionType(questionType);
    question.setQuestionAnswerOptions(options);
    return question;
  }

  private List<AnswerOption> existingAnswerOptions(List<Long> optionIds) {

    return optionIds.stream()
        .map(optionId -> AnswerOption.builder().id(optionId).build())
        .toList();
  }
}