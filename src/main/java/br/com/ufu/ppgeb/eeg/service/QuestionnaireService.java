package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Questionnaire operations.
 */
public interface QuestionnaireService {

  /**
   * Finds questionnaires matching the given filters.
   *
   * @param id the id filter
   * @param title the title filter
   * @param pageable the pagination information
   * @return the page of questionnaires
   */
  Page<Questionnaire> findByFilter(Long id, String title, Pageable pageable);

  /**
   * Finds a questionnaire by id.
   *
   * @param id the questionnaire id
   * @return the questionnaire
   */
  Questionnaire findById(Long id);

  /**
   * Saves a questionnaire.
   *
   * @param questionnaire the questionnaire to save
   * @return the saved questionnaire
   */
  Questionnaire save(Questionnaire questionnaire);

  /**
   * Updates a questionnaire.
   *
   * @param questionnaire the questionnaire to update
   * @return the updated questionnaire
   */
  Questionnaire update(Questionnaire questionnaire);

  /**
   * Deletes a questionnaire by id.
   *
   * @param id the questionnaire id
   */
  void delete(Long id);
}