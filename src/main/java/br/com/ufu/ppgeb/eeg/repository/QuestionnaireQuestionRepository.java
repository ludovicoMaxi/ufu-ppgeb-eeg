package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for QuestionnaireQuestion entities.
 */
public interface QuestionnaireQuestionRepository extends
    JpaRepository<QuestionnaireQuestion, Long> {

  /**
   * Checks whether the given question is linked to the given questionnaire.
   *
   * @param questionnaire the questionnaire
   * @param question the question
   * @return true if the link exists
   */
  boolean existsByQuestionnaireAndQuestion(Questionnaire questionnaire,
      Question question);

  /**
   * Checks whether the given question belongs to any questionnaire.
   *
   * @param question the question
   * @return true if the question is linked to a questionnaire
   */
  boolean existsByQuestion(Question question);
}