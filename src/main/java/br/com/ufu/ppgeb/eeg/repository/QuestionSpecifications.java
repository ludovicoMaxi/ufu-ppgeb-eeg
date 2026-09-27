package br.com.ufu.ppgeb.eeg.repository;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Question;
import br.com.ufu.ppgeb.eeg.model.QuestionType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for Question queries.
 */
public final class QuestionSpecifications {

  private static final String ID = "id";
  private static final String TEXT = "text";
  private static final String QUESTION_TYPE = "questionType";
  private static final String ACTIVE = "active";
  private static final String PERCENT = "%";

  private QuestionSpecifications() {
  }

  /**
   * Builds a specification that filters questions by the given filters.
   *
   * @param id the id filter
   * @param text the text filter
   * @param questionType the question type filter
   * @param active the active flag filter
   * @return the specification
   */
  public static Specification<Question> withFilters(Long id, String text,
      QuestionType questionType, Boolean active) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (nonNull(id)) {
        predicates.add(cb.equal(root.get(ID), id));
      }
      if (StringUtils.isNotBlank(text)) {
        predicates.add(cb.like(cb.lower(root.get(TEXT)),
            PERCENT + text.toLowerCase() + PERCENT));
      }
      if (nonNull(questionType)) {
        predicates.add(cb.equal(root.get(QUESTION_TYPE), questionType));
      }
      if (nonNull(active)) {
        predicates.add(cb.equal(root.get(ACTIVE), active));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}