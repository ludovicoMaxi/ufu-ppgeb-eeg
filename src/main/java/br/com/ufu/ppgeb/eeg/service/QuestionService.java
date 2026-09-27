package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Question operations.
 */
public interface QuestionService {

  /**
   * Finds questions matching the given filters.
   *
   * @param id the id filter
   * @param text the text filter
   * @param questionType the question type filter
   * @param active the active flag filter
   * @param pageable the pagination information
   * @return the page of questions
   */
  Page<Question> findByFilter(Long id, String text, QuestionType questionType,
      Boolean active, Pageable pageable);

  /**
   * Finds a question by id.
   *
   * @param id the question id
   * @return the question
   */
  Question findById(Long id);

  /**
   * Saves a question.
   *
   * @param question the question to save
   * @return the saved question
   */
  Question save(Question question);

  /**
   * Updates a question.
   *
   * @param question the question to update
   * @return the updated question
   */
  Question update(Question question);

  /**
   * Deletes a question by id.
   *
   * @param id the question id
   */
  void delete(Long id);
}