package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for QuestionnaireAnswer operations.
 */
public interface QuestionnaireAnswerService {

  /**
   * Finds the answers of the questionnaire applied to the given object (type and id).
   *
   * @param questionnaireId the questionnaire id
   * @param objectType the object type
   * @param objectId the object id
   * @return the list of answers
   */
  List<QuestionnaireAnswer> findAppliedAnswers(Long questionnaireId,
      Long objectType, Long objectId);

  /**
   * Updates the answers of the questionnaire applied to the given object, preserving
   * the existing answer records.
   *
   * @param questionnaireId the questionnaire id
   * @param objectType the object type
   * @param objectId the object id
   * @param answers the answers to update
   * @return the updated list of answers
   */
  List<QuestionnaireAnswer> updateList(Long questionnaireId, Long objectType,
      Long objectId, List<QuestionnaireAnswer> answers);

  /**
   * Finds distinct questionnaires applied to the given object (type and id).
   *
   * @param objectType the object type
   * @param objectId the object id
   * @param pageable the pagination information
   * @return the page of questionnaires
   */
  Page<Questionnaire> findAppliedQuestionnaires(Long objectType, Long objectId,
      Pageable pageable);
}