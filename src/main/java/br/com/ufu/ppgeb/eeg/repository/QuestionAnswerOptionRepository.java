package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.AnswerOption;
import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionAnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for QuestionAnswerOption entities.
 */
public interface QuestionAnswerOptionRepository
    extends JpaRepository<QuestionAnswerOption, Long> {

  /**
   * Finds the answer option links of a question.
   *
   * @param question the question
   * @return the list of links
   */
  List<QuestionAnswerOption> findByQuestion(Question question);

  /**
   * Checks whether the given answer option is linked to the given question.
   *
   * @param question the question
   * @param answerOption the answer option
   * @return true if the link exists
   */
  boolean existsByQuestionAndAnswerOption(Question question, AnswerOption answerOption);

  /**
   * Checks whether the given answer option is linked to any question.
   *
   * @param answerOption the answer option
   * @return true if the answer option is linked to a question
   */
  boolean existsByAnswerOption(AnswerOption answerOption);
}