package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for AnswerOption operations.
 */
public interface AnswerOptionService {

  /**
   * Lists answer options paginated.
   *
   * @param pageable the pagination information
   * @return the page of answer options
   */
  Page<AnswerOption> findAll(Pageable pageable);

  /**
   * Finds an answer option by id.
   *
   * @param id the answer option id
   * @return the answer option
   */
  AnswerOption findById(Long id);

  /**
   * Saves an answer option.
   *
   * @param answerOption the answer option to save
   * @return the saved answer option
   */
  AnswerOption save(AnswerOption answerOption);

  /**
   * Updates an answer option.
   *
   * @param answerOption the answer option to update
   * @return the updated answer option
   */
  AnswerOption update(AnswerOption answerOption);

  /**
   * Deletes an answer option by id.
   *
   * @param id the answer option id
   */
  void delete(Long id);
}