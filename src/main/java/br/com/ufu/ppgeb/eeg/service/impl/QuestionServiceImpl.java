package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import br.com.ufu.ppgeb.eeg.repository.AnswerOptionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionSpecifications;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireQuestionRepository;
import br.com.ufu.ppgeb.eeg.service.QuestionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of QuestionService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {

  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Question";
  private static final String MSG_QUESTION_NULL = "question cannot be null.";
  private static final String MSG_DUPLICATE_OPTIONS = "The question cannot have duplicate options.";
  private static final String MSG_ANSWER_OPTION_NOT_FOUND = "Answer option not found: ";
  private static final String MSG_ANSWER_OPTION_ID_NULL = "answerOptionId cannot be null.";

  private final QuestionRepository questionRepository;
  private final AnswerOptionRepository answerOptionRepository;
  private final QuestionnaireQuestionRepository questionnaireQuestionRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Question> findByFilter(Long id, String text,
      QuestionType questionType, Boolean active, Pageable pageable) {

    return questionRepository.findAll(
        QuestionSpecifications.withFilters(id, text, questionType, active), pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Question findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return questionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Question save(Question question) {

    Assert.notNull(question, MSG_QUESTION_NULL);
    Assert.isNull(question.getId(), "id must be null");
    return persist(question);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Question update(Question question) {

    Assert.notNull(question, MSG_QUESTION_NULL);
    Assert.notNull(question.getId(), "question ID cannot be null.");

    validateQuestion(question);
    validateOptions(question);

    Long questionId = question.getId();
    Question oldQuestion = findById(questionId);

    oldQuestion.setText(question.getText());
    oldQuestion.setDescription(question.getDescription());
    oldQuestion.setQuestionType(question.getQuestionType());
    oldQuestion.setActive(question.getActive());
    mergeOptions(oldQuestion, question);

    Question saved = questionRepository.save(oldQuestion);
    log.info("Questão atualizada com id={}", questionId);
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    Question question = findById(id);
    Assert.isTrue(!questionnaireQuestionRepository.existsByQuestion(question),
        "Question is referenced by questionnaires and cannot be removed.");
    questionRepository.delete(question);
    log.info("Questão removida com id={}", id);
  }

  private Question persist(Question question) {

    validateQuestion(question);
    validateOptions(question);

    Question saved = questionRepository.save(question);
    log.info("Questão criada com id={}", saved.getId());
    return saved;
  }

  private void validateQuestion(Question question) {

    Assert.notNull(question, MSG_QUESTION_NULL);
    Assert.hasText(question.getText(), "text cannot be empty.");
    Assert.notNull(question.getQuestionType(), "questionType cannot be null.");

    List<QuestionAnswerOption> options = question.getQuestionAnswerOptions();
    if (question.getQuestionType() == QuestionType.FREE_TEXT) {
      Assert.isTrue(isEmpty(options), "FREE_TEXT question cannot have options.");
    } else {
      Assert.isTrue(isNotEmpty(options), "selection question must have options.");
    }
  }

  private void validateOptions(Question question) {

    List<QuestionAnswerOption> options = question.getQuestionAnswerOptions();
    if (isEmpty(options)) {
      return;
    }

    List<Long> optionIds = options.stream()
        .map(QuestionAnswerOption::getAnswerOption)
        .filter(Objects::nonNull)
        .map(AnswerOption::getId)
        .toList();
    Assert.isTrue(optionIds.stream().noneMatch(Objects::isNull), MSG_ANSWER_OPTION_ID_NULL);
    Assert.isTrue(optionIds.size() == optionIds.stream().distinct().count(),
        MSG_DUPLICATE_OPTIONS);

    Set<Long> foundIds = answerOptionRepository.findAllById(optionIds).stream()
        .map(AnswerOption::getId)
        .collect(Collectors.toSet());
    List<Long> missingIds = optionIds.stream()
        .filter(optionId -> !foundIds.contains(optionId))
        .toList();
    Assert.isTrue(isEmpty(missingIds),
        MSG_ANSWER_OPTION_NOT_FOUND + missingIds.stream().map(String::valueOf)
            .collect(Collectors.joining(", ")));
  }

  private void mergeOptions(Question oldQuestion, Question question) {

    List<QuestionAnswerOption> incoming = Optional.ofNullable(
        question.getQuestionAnswerOptions()).orElse(List.of());
    List<QuestionAnswerOption> existing = Optional.ofNullable(
        oldQuestion.getQuestionAnswerOptions()).orElse(new ArrayList<>());

    List<QuestionAnswerOption> toKeep = existing.stream()
        .filter(link -> incoming.stream().anyMatch(in ->
            sameOption(link, in)))
        .toList();

    oldQuestion.getQuestionAnswerOptions().clear();
    oldQuestion.getQuestionAnswerOptions().addAll(toKeep);

    for (QuestionAnswerOption incomingLink : incoming) {
      boolean kept = toKeep.stream().anyMatch(keptLink ->
          sameOption(keptLink, incomingLink));
      if (!kept) {
        oldQuestion.getQuestionAnswerOptions().add(
            QuestionAnswerOption.builder()
                .question(oldQuestion)
                .answerOption(incomingLink.getAnswerOption())
                .sortOrder(incomingLink.getSortOrder())
                .build());
      }
    }
  }

  private boolean sameOption(QuestionAnswerOption first, QuestionAnswerOption second) {

    return nonNull(second.getAnswerOption())
        && Objects.equals(first.getAnswerOption().getId(), second.getAnswerOption().getId())
        && Objects.equals(first.getSortOrder(), second.getSortOrder());
  }
}