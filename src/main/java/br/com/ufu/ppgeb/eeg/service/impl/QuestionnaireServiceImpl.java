package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.nonNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import br.com.ufu.ppgeb.eeg.repository.QuestionRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireAnswerRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireRepository;
import br.com.ufu.ppgeb.eeg.repository.QuestionnaireSpecifications;
import br.com.ufu.ppgeb.eeg.service.QuestionnaireService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of QuestionnaireService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class QuestionnaireServiceImpl implements QuestionnaireService {

  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Questionnaire";
  private static final String MSG_QUESTIONNAIRE_NULL = "questionnaire cannot be null.";
  private static final String MSG_DUPLICATE_QUESTIONS =
      "The questionnaire cannot have duplicate questions.";
  private static final String MSG_QUESTION_NOT_FOUND = "Question not found: ";
  private static final String MSG_QUESTION_ID_NULL = "questionId cannot be null.";

  private final QuestionnaireRepository questionnaireRepository;
  private final QuestionRepository questionRepository;
  private final QuestionnaireAnswerRepository questionnaireAnswerRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<Questionnaire> findByFilter(Long id, String title, Pageable pageable) {

    return questionnaireRepository.findAll(
        QuestionnaireSpecifications.withFilters(id, title), pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public Questionnaire findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return questionnaireRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Questionnaire save(Questionnaire questionnaire) {

    Assert.notNull(questionnaire, MSG_QUESTIONNAIRE_NULL);
    Assert.isNull(questionnaire.getId(), "id must be null");
    return persist(questionnaire);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Questionnaire update(Questionnaire questionnaire) {

    Assert.notNull(questionnaire, MSG_QUESTIONNAIRE_NULL);
    Assert.notNull(questionnaire.getId(), "questionnaire ID cannot be null.");

    validateQuestionnaire(questionnaire);
    validateQuestionLinks(questionnaire);

    Long questionnaireId = questionnaire.getId();
    Questionnaire old = questionnaireRepository.findById(questionnaireId)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, questionnaireId));

    old.setTitle(questionnaire.getTitle());
    old.setDescription(questionnaire.getDescription());
    mergeQuestionLinks(old, questionnaire);

    Questionnaire saved = questionnaireRepository.save(old);
    log.info("Questionário atualizado com id={}", questionnaireId);
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    Questionnaire questionnaire = questionnaireRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    Assert.isTrue(!questionnaireAnswerRepository.existsByQuestionnaire(questionnaire),
        "Questionnaire has answers and cannot be removed.");
    questionnaireRepository.delete(questionnaire);
    log.info("Questionário removido com id={}", id);
  }

  private Questionnaire persist(Questionnaire questionnaire) {

    validateQuestionnaire(questionnaire);
    validateQuestionLinks(questionnaire);

    Questionnaire saved = questionnaireRepository.save(questionnaire);
    log.info("Questionário criado com id={}", saved.getId());
    return saved;
  }

  private void validateQuestionnaire(Questionnaire questionnaire) {

    Assert.notNull(questionnaire, MSG_QUESTIONNAIRE_NULL);
    Assert.hasText(questionnaire.getTitle(), "title cannot be empty.");
  }

  private void validateQuestionLinks(Questionnaire questionnaire) {

    List<QuestionnaireQuestion> links = questionnaire.getQuestionnaireQuestions();
    if (isEmpty(links)) {
      return;
    }

    List<Long> questionIds = links.stream()
        .map(QuestionnaireQuestion::getQuestion)
        .filter(Objects::nonNull)
        .map(Question::getId)
        .toList();
    Assert.isTrue(questionIds.stream().noneMatch(Objects::isNull), MSG_QUESTION_ID_NULL);
    Assert.isTrue(questionIds.size() == questionIds.stream().distinct().count(),
        MSG_DUPLICATE_QUESTIONS);

    Set<Long> foundIds = questionRepository.findAllById(questionIds).stream()
        .map(Question::getId)
        .collect(Collectors.toSet());
    List<Long> missingIds = questionIds.stream()
        .filter(questionId -> !foundIds.contains(questionId))
        .toList();
    Assert.isTrue(isEmpty(missingIds),
        MSG_QUESTION_NOT_FOUND + missingIds.stream().map(String::valueOf)
            .collect(Collectors.joining(", ")));
  }

  private void mergeQuestionLinks(Questionnaire old, Questionnaire source) {

    List<QuestionnaireQuestion> incoming = Optional.ofNullable(
        source.getQuestionnaireQuestions()).orElse(List.of());
    List<QuestionnaireQuestion> existing = Optional.ofNullable(
        old.getQuestionnaireQuestions()).orElse(new ArrayList<>());

    List<QuestionnaireQuestion> toKeep = existing.stream()
        .filter(link -> incoming.stream().anyMatch(in ->
            sameQuestion(link, in)))
        .toList();

    old.getQuestionnaireQuestions().clear();
    old.getQuestionnaireQuestions().addAll(toKeep);

    for (QuestionnaireQuestion incomingLink : incoming) {
      Optional<QuestionnaireQuestion> kept = toKeep.stream()
          .filter(keptLink -> sameQuestion(keptLink, incomingLink))
          .findFirst();
      if (kept.isPresent()) {
        kept.get().setSortOrder(incomingLink.getSortOrder());
      } else {
        old.getQuestionnaireQuestions().add(
            QuestionnaireQuestion.builder()
                .questionnaire(old)
                .question(incomingLink.getQuestion())
                .sortOrder(incomingLink.getSortOrder())
                .build());
      }
    }
  }

  private boolean sameQuestion(QuestionnaireQuestion first,
      QuestionnaireQuestion second) {

    return nonNull(second.getQuestion())
        && Objects.equals(first.getQuestion().getId(), second.getQuestion().getId());
  }
}