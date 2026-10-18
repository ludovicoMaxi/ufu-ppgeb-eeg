package br.com.ufu.ppgeb.eeg.service.impl;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.repository.AnswerOptionRepository;
import br.com.ufu.ppgeb.eeg.repository.AnswerOptionSpecifications;
import br.com.ufu.ppgeb.eeg.repository.QuestionAnswerOptionRepository;
import br.com.ufu.ppgeb.eeg.service.AnswerOptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of AnswerOptionService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AnswerOptionServiceImpl implements AnswerOptionService {

  private static final String MSG_NULL = "answerOption cannot be null.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "AnswerOption";

  private final AnswerOptionRepository answerOptionRepository;
  private final QuestionAnswerOptionRepository questionAnswerOptionRepository;

  @Override
  @Transactional(readOnly = true)
  public Page<AnswerOption> findByFilter(Long id, String text, Boolean active,
      Pageable pageable) {

    return answerOptionRepository.findAll(
        AnswerOptionSpecifications.withFilters(id, text, active), pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public AnswerOption findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return answerOptionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AnswerOption save(AnswerOption answerOption) {

    Assert.notNull(answerOption, MSG_NULL);
    Assert.isNull(answerOption.getId(), "id must be null");

    validate(answerOption);

    AnswerOption saved = answerOptionRepository.save(answerOption);
    log.info("Alternativa de resposta criada com id={}", saved.getId());
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public AnswerOption update(AnswerOption answerOption) {

    Assert.notNull(answerOption, MSG_NULL);
    Assert.notNull(answerOption.getId(), "answerOption ID cannot be null.");

    validate(answerOption);

    Long answerOptionId = answerOption.getId();
    AnswerOption old = answerOptionRepository.findById(answerOptionId)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, answerOptionId));

    old.setText(answerOption.getText());
    old.setActive(answerOption.getActive());

    AnswerOption saved = answerOptionRepository.save(old);
    log.info("Alternativa de resposta atualizada com id={}", answerOptionId);
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    AnswerOption answerOption = answerOptionRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
    Assert.isTrue(!questionAnswerOptionRepository.existsByAnswerOption(answerOption),
        "Answer option is referenced by questions and cannot be removed.");
    answerOptionRepository.delete(answerOption);
    log.info("Alternativa de resposta removida com id={}", id);
  }

  private void validate(AnswerOption answerOption) {

    Assert.notNull(answerOption, "Answer option cannot be null.");
    Assert.hasText(answerOption.getText(), "text cannot be empty.");
  }
}