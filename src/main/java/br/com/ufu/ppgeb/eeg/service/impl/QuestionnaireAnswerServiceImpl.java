package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireAnswerRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireRepository;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireAnswerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of QuestionnaireAnswerService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuestionnaireAnswerServiceImpl implements QuestionnaireAnswerService {

  private static final String MSG_QUESTIONNAIRE_ID_NULL = "questionnaireId cannot be null.";
  private static final String MSG_OBJECT_TYPE_NULL = "objectType cannot be null.";
  private static final String MSG_OBJECT_ID_NULL = "objectId cannot be null.";
  private static final int ONE_OPTION = 1;

  private final QuestionnaireRepository questionnaireRepository;
  private final QuestionnaireAnswerRepository questionnaireAnswerRepository;

  @Override
  @Transactional(readOnly = true)
  public List<QuestionnaireAnswer> findAppliedAnswers(Long questionnaireId,
      Long objectType, Long objectId) {

    Assert.notNull(questionnaireId, MSG_QUESTIONNAIRE_ID_NULL);
    Assert.notNull(objectType, MSG_OBJECT_TYPE_NULL);
    Assert.notNull(objectId, MSG_OBJECT_ID_NULL);

    Questionnaire questionnaire = findQuestionnaire(questionnaireId);
    return questionnaireAnswerRepository
        .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, objectType, objectId);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public List<QuestionnaireAnswer> updateList(Long questionnaireId, Long objectType,
      Long objectId, List<QuestionnaireAnswer> answers) {

    Assert.notNull(questionnaireId, MSG_QUESTIONNAIRE_ID_NULL);
    Assert.notNull(objectType, MSG_OBJECT_TYPE_NULL);
    Assert.notNull(objectId, MSG_OBJECT_ID_NULL);

    Questionnaire questionnaire = findQuestionnaire(questionnaireId);

    var oldQuestionnaireAnswered = questionnaireAnswerRepository
            .findByQuestionnaireAndObjectTypeAndObjectId(questionnaire, objectType, objectId);

    Map<Long, QuestionnaireAnswer> oldAnswersById = new HashMap<>();
    for (QuestionnaireAnswer oldAnswer : oldQuestionnaireAnswered) {
      oldAnswersById.put(oldAnswer.getId(), oldAnswer);
    }

    List<QuestionnaireAnswer> savedAnswers = new ArrayList<>();

    if (isNotEmpty(answers)) {

      for (QuestionnaireAnswer answer : answers) {

        validateAnswer(questionnaire, answer);

        if (nonNull(answer.getId())) {

          QuestionnaireAnswer oldAnswer = oldAnswersById.remove(answer.getId());
          if (isNull(oldAnswer)) {
            throw new IllegalArgumentException("Answer with id=" + answer.getId()
                + " does not belong to the questionnaire of the object.");
          }

          mergeAnswer(oldAnswer, answer);
          savedAnswers.add(questionnaireAnswerRepository.save(oldAnswer));
        } else {
          answer.setQuestionnaire(questionnaire);
          savedAnswers.add(questionnaireAnswerRepository.save(answer));
        }
      }
    }

    questionnaireAnswerRepository.deleteAll(oldAnswersById.values());

    log.info("Respostas do questionário atualizadas; questionnaireId={}, "
        + "objectType={}, objectId={}, quantidade={}",
        questionnaireId, objectType, objectId, savedAnswers.size());
    return savedAnswers;
  }

  private Questionnaire findQuestionnaire(Long questionnaireId) {

    return questionnaireRepository.findById(questionnaireId)
        .orElseThrow(() -> new ResourceNotFoundException("Questionnaire", questionnaireId));
  }

  private void validateAnswer(Questionnaire questionnaire, QuestionnaireAnswer answer) {

    Assert.notNull(answer, "answer cannot be null.");
    Assert.notNull(answer.getQuestion(), "question cannot be null.");
    Assert.notNull(answer.getQuestion().getId(), "question ID cannot be null.");

    Question question = findLinkedQuestion(questionnaire, answer.getQuestion().getId());

    List<QuestionnaireAnswerOption> selecteds = answer.getAnswerOptions();
    switch (question.getQuestionType()) {
      case FREE_TEXT -> {
        Assert.hasText(answer.getFreeText(),
            "free text is required for FREE_TEXT questions.");
        Assert.isTrue(isEmpty(selecteds),
            "FREE_TEXT question cannot have selected options.");
      }
      case SINGLE_SELECT -> {
        Assert.isTrue(isNotEmpty(selecteds) && selecteds.size() == ONE_OPTION,
            "SINGLE_SELECT question requires exactly one selected option.");
      }
      case MULTI_SELECT -> Assert.isTrue(isNotEmpty(selecteds),
          "MULTI_SELECT question requires at least one selected option.");
      default -> throw new IllegalArgumentException(
          "Unsupported question type: " + question.getQuestionType());
    }

    validateSelectedOptions(question, selecteds);
  }

  private Question findLinkedQuestion(Questionnaire questionnaire, Long questionId) {

    return questionLinks(questionnaire).stream()
        .filter(Objects::nonNull)
        .map(QuestionnaireQuestion::getQuestion)
        .filter(Objects::nonNull)
        .filter(question -> Objects.equals(question.getId(), questionId))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            "question does not belong to the questionnaire."));
  }

  private List<QuestionnaireQuestion> questionLinks(Questionnaire questionnaire) {

    return Optional.ofNullable(questionnaire.getQuestionnaireQuestions()).orElse(List.of());
  }

  private void validateSelectedOptions(Question question,
      List<QuestionnaireAnswerOption> selecteds) {

    if (isEmpty(selecteds)) {
      return;
    }

    List<Long> optionIds = selecteds.stream()
        .filter(option -> nonNull(option.getAnswerOption()))
        .map(option -> option.getAnswerOption().getId())
        .toList();
    Assert.isTrue(optionIds.stream().noneMatch(Objects::isNull), "answerOptionId cannot be null.");
    Assert.isTrue(optionIds.size() == optionIds.stream().distinct().count(),
        "answer cannot have duplicate selected options.");

    List<Long> availableOptionIds = Optional.ofNullable(question.getQuestionAnswerOptions())
        .orElse(List.of()).stream()
        .filter(Objects::nonNull)
        .map(QuestionAnswerOption::getAnswerOption)
        .filter(Objects::nonNull)
        .map(AnswerOption::getId)
        .filter(Objects::nonNull)
        .toList();
    for (Long optionId : optionIds) {
      Assert.isTrue(availableOptionIds.contains(optionId),
          "Answer option is not available for the question.");
    }
  }

  private void mergeAnswer(QuestionnaireAnswer old, QuestionnaireAnswer source) {

    old.setFreeText(source.getFreeText());

    List<QuestionnaireAnswerOption> incoming = Optional.ofNullable(
        source.getAnswerOptions()).orElse(List.of());
    List<QuestionnaireAnswerOption> existing = Optional.ofNullable(
        old.getAnswerOptions()).orElse(List.of());

    List<QuestionnaireAnswerOption> toKeep = existing.stream()
        .filter(oldOption -> incoming.stream().anyMatch(in ->
            sameOptionId(oldOption, in)))
        .toList();

    List<QuestionnaireAnswerOption> merged = new ArrayList<>(toKeep);
    for (QuestionnaireAnswerOption incomingOption : incoming) {
      boolean kept = toKeep.stream().anyMatch(keptOption ->
          sameOptionId(keptOption, incomingOption));
      if (!kept) {
        merged.add(
            QuestionnaireAnswerOption.builder()
                .questionnaireAnswer(old)
                .answerOption(incomingOption.getAnswerOption())
                .build());
      }
    }
    old.setAnswerOptions(merged);
  }

  private boolean sameOptionId(QuestionnaireAnswerOption first,
      QuestionnaireAnswerOption second) {

    Long firstId = optionIdOf(first);
    return nonNull(firstId) && Objects.equals(firstId, optionIdOf(second));
  }

  private Long optionIdOf(QuestionnaireAnswerOption questionnaireAnswerOption) {

    if (isNull(questionnaireAnswerOption)
        || isNull(questionnaireAnswerOption.getAnswerOption())) {
      return null;
    }
    return questionnaireAnswerOption.getAnswerOption().getId();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Questionnaire> findAppliedQuestionnaires(Long objectType, Long objectId,
      Pageable pageable) {

    Assert.notNull(objectType, MSG_OBJECT_TYPE_NULL);
    Assert.notNull(objectId, MSG_OBJECT_ID_NULL);

    return questionnaireAnswerRepository
        .findDistinctQuestionnaireByObjectTypeAndObjectId(objectType, objectId, pageable);
  }
}