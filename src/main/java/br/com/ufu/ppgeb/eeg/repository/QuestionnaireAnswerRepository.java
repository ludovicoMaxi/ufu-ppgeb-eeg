package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for QuestionnaireAnswer entities.
 */
public interface QuestionnaireAnswerRepository extends
    JpaRepository<QuestionnaireAnswer, Long> {

  /**
   * Finds the answers of the given questionnaire and object (type and id).
   *
   * @param questionnaire the questionnaire
   * @param objectType the object type
   * @param objectId the object id
   * @return the list of answers
   */
  List<QuestionnaireAnswer> findByQuestionnaireAndObjectTypeAndObjectId(
      Questionnaire questionnaire, Long objectType, Long objectId);

  /**
   * Checks whether the given questionnaire and object (type and id) have any answer.
   *
   * @param questionnaire the questionnaire
   * @param objectType the object type
   * @param objectId the object id
   * @return true if there is at least one answer
   */
  boolean existsByQuestionnaireAndObjectTypeAndObjectId(
      Questionnaire questionnaire, Long objectType, Long objectId);

  /**
   * Checks whether the given questionnaire has any answer.
   *
   * @param questionnaire the questionnaire
   * @return true if there is at least one answer
   */
  boolean existsByQuestionnaire(Questionnaire questionnaire);

  /**
   * Finds distinct questionnaires applied to the given object (type and id).
   *
   * @param objectType the object type
   * @param objectId the object id
   * @param pageable the pagination information
   * @return the page of questionnaires
   */
  @Query("SELECT DISTINCT qa.questionnaire FROM QuestionnaireAnswer qa "
      + "WHERE qa.objectType = :objectType AND qa.objectId = :objectId")
  Page<Questionnaire> findDistinctQuestionnaireByObjectTypeAndObjectId(
      @Param("objectType") Long objectType,
      @Param("objectId") Long objectId,
      Pageable pageable);
}