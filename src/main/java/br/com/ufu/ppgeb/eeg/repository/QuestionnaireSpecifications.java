package br.com.ufu.ppgeb.eeg.repository;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Questionnaire;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for Questionnaire queries.
 */
public final class QuestionnaireSpecifications {

  private static final String ID = "id";
  private static final String TITLE = "title";
  private static final String PERCENT = "%";

  private QuestionnaireSpecifications() {
  }

  /**
   * Builds a specification that filters questionnaires by the given filters.
   *
   * @param id the id filter
   * @param title the title filter
   * @return the specification
   */
  public static Specification<Questionnaire> withFilters(Long id, String title) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (nonNull(id)) {
        predicates.add(cb.equal(root.get(ID), id));
      }
      if (StringUtils.isNotBlank(title)) {
        predicates.add(cb.like(cb.lower(root.get(TITLE)),
            PERCENT + title.toLowerCase() + PERCENT));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}