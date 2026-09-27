package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswer;
import br.com.ufu.ppgeb.eeg.model.QuestionnaireAnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for QuestionnaireAnswerOption entities.
 */
public interface QuestionnaireAnswerOptionRepository extends
    JpaRepository<QuestionnaireAnswerOption, Long> {

  /**
   * Finds the selected answer options of a questionnaire answer.
   *
   * @param questionnaireAnswer the questionnaire answer
   * @return the list of selected options
   */
  List<QuestionnaireAnswerOption> findByQuestionnaireAnswer(
      QuestionnaireAnswer questionnaireAnswer);
}