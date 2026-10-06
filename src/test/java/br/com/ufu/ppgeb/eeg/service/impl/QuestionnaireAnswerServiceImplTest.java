package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireAnswerRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class QuestionnaireAnswerServiceImplTest {

  private static final Long QUESTIONNAIRE_ID = 1L;
  private static final Long OBJECT_TYPE = 2L;
  private static final Long OBJECT_ID = 10L;
  private static final Long ANSWER_ID = 100L;
  private static final Long QUESTION_ID = 201L;
  private static final Long OTHER_QUESTION_ID = 202L;
  private static final Long ANSWER_OPTION_ID = 301L;
  private static final Long OTHER_ANSWER_OPTION_ID = 302L;
  private static final String MSG_NOT_FOUND_QUESTIONNAIRE = "Questionnaire não encontrado(a) com id=";
  private static final String MSG_QUESTION_OUTSIDE = "question does not belong to the questionnaire.";
  private static final String MSG_OPTION_NOT_AVAILABLE = "Answer option is not available for the question.";
  private static final String SELECT_OBSERVATION = "Paciente permaneceu sonolento durante o exame.";
  private static final int ONE_OPTION = 1;
  private static final int TWO_OPTIONS = 2;

  @Mock
  private QuestionnaireRepository questionnaireRepository;

  @Mock
  private QuestionnaireAnswerRepository questionnaireAnswerRepository;

  @InjectMocks
  private QuestionnaireAnswerServiceImpl questionnaireAnswerService;

  @Test
  @DisplayName("Given existing answers when findAppliedAnswers then return the answers")
  void givenExistingAnswers_whenFindAppliedAnswers_thenReturnTheAnswers() {
    Questionnaire questionnaire = createQuestionnaire();
    QuestionnaireAnswer answer = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(answer));

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .findAppliedAnswers(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID);

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(ANSWER_ID);
  }

  @Test
  @DisplayName("Given null questionnaire id when findAppliedAnswers then throw exception")
  void givenNullQuestionnaireId_whenFindAppliedAnswers_thenThrowException() {

    assertThatThrownBy(() -> questionnaireAnswerService
        .findAppliedAnswers(null, OBJECT_TYPE, OBJECT_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("questionnaireId cannot be null.");

    verify(questionnaireAnswerRepository, never()).findAll();
  }

  @Test
  @DisplayName("Given null object type when findAppliedAnswers then throw exception")
  void givenNullObjectType_whenFindAppliedAnswers_thenThrowException() {

    assertThatThrownBy(() -> questionnaireAnswerService
        .findAppliedAnswers(QUESTIONNAIRE_ID, null, OBJECT_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("objectType cannot be null.");
  }

  @Test
  @DisplayName("Given nonexistent questionnaire when findAppliedAnswers then throw exception")
  void givenNonexistentQuestionnaire_whenFindAppliedAnswers_thenThrowException() {
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionnaireAnswerService
        .findAppliedAnswers(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(MSG_NOT_FOUND_QUESTIONNAIRE + QUESTIONNAIRE_ID);
  }

  @Test
  @DisplayName("Given new free text answers when updateList then save them")
  void givenNewFreeTextAnswers_whenUpdateList_thenSaveThem() {
    Questionnaire questionnaire = createQuestionnaire(
        createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of()));
    final QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of());
    answer.setFreeText("Paciente colaborou");
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(answer);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer));

    assertThat(result).hasSize(1);
    assertThat(answer.getQuestionnaire()).isEqualTo(questionnaire);
    verify(questionnaireAnswerRepository).save(answer);
    verify(questionnaireAnswerRepository).deleteAll(any());
  }

  @Test
  @DisplayName("Given new single select answer when updateList then save it with the chosen option")
  void givenNewSingleSelectAnswer_whenUpdateList_thenSaveItWithTheChosenOption() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(answer);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getAnswerOptions()).hasSize(1);
    assertThat(result.get(0).getAnswerOptions().get(0).getAnswerOption().getId())
        .isEqualTo(ANSWER_OPTION_ID);
  }

  @Test
  @DisplayName("Given new multi select answer when updateList then allow more than one option")
  void givenNewMultiSelectAnswer_whenUpdateList_thenAllowMoreThanOneOption() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.MULTI_SELECT, List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID,
        List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(answer);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getAnswerOptions()).hasSize(TWO_OPTIONS);
  }

  @Test
  @DisplayName("Given new single select answer with observation when updateList then save the "
      + "option and the observation")
  void givenNewSingleSelectAnswerWithObservation_whenUpdateList_thenSaveTheOptionAndTheObservation() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    answer.setFreeText(SELECT_OBSERVATION);
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(answer);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFreeText()).isEqualTo(SELECT_OBSERVATION);
    assertThat(result.get(0).getAnswerOptions()).hasSize(ONE_OPTION);
  }

  @Test
  @DisplayName("Given new multi select answer with description when updateList then allow more than "
      + "one option and the description")
  void givenNewMultiSelectAnswerWithDescription_whenUpdateList_thenAllowMoreThanOneOptionAndTheDescription() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.MULTI_SELECT, List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID,
        List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
    answer.setFreeText(SELECT_OBSERVATION);
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(answer);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFreeText()).isEqualTo(SELECT_OBSERVATION);
    assertThat(result.get(0).getAnswerOptions()).hasSize(TWO_OPTIONS);
  }

  @Test
  @DisplayName("Given existing answer when updateList then merge and save the answer")
  void givenExistingAnswer_whenUpdateList_thenMergeAndSaveTheAnswer() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer old = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    QuestionnaireAnswer incoming = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(old));
    when(questionnaireAnswerRepository.save(old)).thenReturn(old);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(incoming));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getId()).isEqualTo(ANSWER_ID);
    assertThat(result.get(0).getAnswerOptions()).hasSize(1);
    assertThat(result.get(0).getAnswerOptions().get(0).getAnswerOption().getId())
        .isEqualTo(ANSWER_OPTION_ID);
    verify(questionnaireAnswerRepository).save(old);
  }

  @Test
  @DisplayName("Given an existing answer when updateList then merge the options in the stored "
      + "collection instance instead of replacing it")
  void givenAnExistingAnswer_whenUpdateList_thenMergeTheOptionsInTheStoredCollectionInstance() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.MULTI_SELECT, List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID)));
    QuestionnaireAnswer old = createAnswer(ANSWER_ID, QUESTION_ID,
        List.of(ANSWER_OPTION_ID, OTHER_ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(old));
    when(questionnaireAnswerRepository.save(old)).thenReturn(old);
    List<QuestionnaireAnswerOption> storedOptions = old.getAnswerOptions();
    QuestionnaireAnswer incoming = createAnswer(ANSWER_ID, QUESTION_ID,
        List.of(OTHER_ANSWER_OPTION_ID));

    questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(incoming));

    assertThat(old.getAnswerOptions()).isSameAs(storedOptions);
    assertThat(storedOptions).hasSize(ONE_OPTION);
    assertThat(storedOptions.get(0).getAnswerOption().getId())
        .isEqualTo(OTHER_ANSWER_OPTION_ID);
    verify(questionnaireAnswerRepository).save(old);
  }

  @Test
  @DisplayName("Given existing single select answer with a new observation when updateList then "
      + "update the observation")
  void givenExistingSingleSelectAnswerWithObservation_whenUpdateList_thenUpdateTheObservation() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer old = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    QuestionnaireAnswer incoming = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    incoming.setFreeText(SELECT_OBSERVATION);
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(old));
    when(questionnaireAnswerRepository.save(old)).thenReturn(old);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(incoming));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getFreeText()).isEqualTo(SELECT_OBSERVATION);
    assertThat(result.get(0).getAnswerOptions()).hasSize(ONE_OPTION);
    assertThat(result.get(0).getAnswerOptions().get(0).getAnswerOption().getId())
        .isEqualTo(ANSWER_OPTION_ID);
    verify(questionnaireAnswerRepository).save(old);
  }

  @Test
  @DisplayName("Given existing answer with incomplete stored options when updateList then merge "
      + "the incoming option without failing")
  void givenExistingAnswerWithIncompleteStoredOptions_whenUpdateList_thenMergeTheIncomingOptionWithoutFailing() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer old = createAnswer(ANSWER_ID, QUESTION_ID, List.of());
    old.setAnswerOptions(new ArrayList<>(Arrays.asList(null,
        QuestionnaireAnswerOption.builder().build())));
    QuestionnaireAnswer incoming = createAnswer(ANSWER_ID, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(old));
    when(questionnaireAnswerRepository.save(old)).thenReturn(old);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(incoming));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getAnswerOptions()).hasSize(ONE_OPTION);
    assertThat(result.get(0).getAnswerOptions().get(0).getAnswerOption().getId())
        .isEqualTo(ANSWER_OPTION_ID);
    verify(questionnaireAnswerRepository).save(old);
  }

  @Test
  @DisplayName("Given answers whose ids are removed when updateList then delete them")
  void givenAnswersWhoseIdsAreRemoved_whenUpdateList_thenDeleteThem() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer removed = createAnswer(ANSWER_ID, OTHER_QUESTION_ID,
        List.of(ANSWER_OPTION_ID));
    QuestionnaireAnswer kept = createAnswer(null, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(removed));
    when(questionnaireAnswerRepository.save(any(QuestionnaireAnswer.class))).thenReturn(kept);

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(kept));

    assertThat(result).hasSize(1);
    verify(questionnaireAnswerRepository).deleteAll(
        argThat(answers -> ((Collection<?>) answers).size() == 1));
  }

  @Test
  @DisplayName("Given answer with id outside the stored answers when updateList then throw "
      + "exception")
  void givenAnswerWithIdOutsideTheStoredAnswers_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer incoming = createAnswer(ANSWER_ID, QUESTION_ID,
        List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(incoming)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Answer with id=" + ANSWER_ID
            + " does not belong to the questionnaire of the object.");

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given free text answer without text when updateList then throw exception")
  void givenFreeTextAnswerWithoutText_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(
        createQuestion(QUESTION_ID, QuestionType.FREE_TEXT, List.of()));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of());
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("free text is required for FREE_TEXT questions.");

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given single select answer without options when updateList then throw exception")
  void givenSingleSelectAnswerWithoutOptions_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(
        createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT, List.of()));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of());
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("SINGLE_SELECT question requires exactly one selected option.");

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given answer with option not available for the question when updateList then throw exception")
  void givenAnswerWithOptionNotAvailableForTheQuestion_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(
        createQuestion(QUESTION_ID, QuestionType.SINGLE_SELECT, List.of()));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_OPTION_NOT_AVAILABLE);

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given answer with option without id when updateList then throw exception")
  void givenAnswerWithOptionWithoutId_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(QUESTION_ID,
        QuestionType.MULTI_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID,
        Arrays.asList(ANSWER_OPTION_ID, null));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("answerOptionId cannot be null.");

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given answer for a question outside the questionnaire when updateList then throw exception")
  void givenAnswerForQuestionOutsideTheQuestionnaire_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire(createQuestion(OTHER_QUESTION_ID,
        QuestionType.SINGLE_SELECT, List.of(ANSWER_OPTION_ID)));
    QuestionnaireAnswer answer = createAnswer(null, QUESTION_ID, List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_QUESTION_OUTSIDE);

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given answer with question without id when updateList then throw exception")
  void givenAnswerWithQuestionWithoutId_whenUpdateList_thenThrowException() {
    Questionnaire questionnaire = createQuestionnaire();
    QuestionnaireAnswer answer = createAnswer(null, null, List.of());
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of(answer)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("question ID cannot be null.");

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given nonexistent questionnaire when updateList then throw exception")
  void givenNonexistentQuestionnaire_whenUpdateList_thenThrowException() {
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(MSG_NOT_FOUND_QUESTIONNAIRE + QUESTIONNAIRE_ID);

    verify(questionnaireAnswerRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given an empty answer list when updateList then delete all existing answers")
  void givenAnEmptyAnswerList_whenUpdateList_thenDeleteAllExistingAnswers() {
    Questionnaire questionnaire = createQuestionnaire();
    QuestionnaireAnswer existing = createAnswer(ANSWER_ID, QUESTION_ID,
        List.of(ANSWER_OPTION_ID));
    when(questionnaireRepository.findById(QUESTIONNAIRE_ID))
        .thenReturn(Optional.of(questionnaire));
    when(questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, OBJECT_TYPE, OBJECT_ID))
        .thenReturn(List.of(existing));

    List<QuestionnaireAnswer> result = questionnaireAnswerService
        .updateList(QUESTIONNAIRE_ID, OBJECT_TYPE, OBJECT_ID, List.of());

    assertThat(result).isEmpty();
    verify(questionnaireAnswerRepository).deleteAll(
        argThat(answers -> ((Collection<?>) answers).size() == 1));
  }

  private QuestionnaireAnswer createAnswer(Long id, Long questionId, List<Long> optionIds) {

    List<QuestionnaireAnswerOption> answerOptions = optionIds.stream()
        .map(optionId -> QuestionnaireAnswerOption.builder()
            .answerOption(AnswerOption.builder().id(optionId).build())
            .build())
        .collect(Collectors.toCollection(ArrayList::new));
    QuestionnaireAnswer answer = Instancio.create(QuestionnaireAnswer.class);
    answer.setId(id);
    answer.setQuestionnaire(Questionnaire.builder().id(QUESTIONNAIRE_ID).build());
    answer.setQuestion(Question.builder().id(questionId).build());
    answer.setObjectType(OBJECT_TYPE);
    answer.setObjectId(OBJECT_ID);
    answer.setFreeText(null);
    answer.setAnswerOptions(answerOptions);
    return answer;
  }

  private Questionnaire createQuestionnaire(Question... questions) {

    List<QuestionnaireQuestion> links = Arrays.stream(questions)
        .map(question -> QuestionnaireQuestion.builder()
            .question(question)
            .sortOrder(1L)
            .build())
        .collect(Collectors.toCollection(ArrayList::new));
    return Questionnaire.builder()
        .id(QUESTIONNAIRE_ID)
        .questionnaireQuestions(links)
        .build();
  }

  private Question createQuestion(Long questionId, QuestionType questionType,
      List<Long> optionIds) {

    List<QuestionAnswerOption> options = optionIds.stream()
        .map(optionId -> QuestionAnswerOption.builder()
            .answerOption(AnswerOption.builder().id(optionId).build())
            .sortOrder(1L)
            .build())
        .collect(Collectors.toCollection(ArrayList::new));
    return Question.builder()
        .id(questionId)
        .questionType(questionType)
        .questionAnswerOptions(options)
        .build();
  }
}
