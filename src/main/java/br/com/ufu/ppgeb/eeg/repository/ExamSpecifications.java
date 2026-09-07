package br.com.ufu.ppgeb.eeg.repository;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for Exam queries.
 */
public final class ExamSpecifications {

  private static final String LIKE_WILDCARD = "%";
  private static final String ID = "id";
  private static final String BED = "bed";
  private static final String PATIENT = "patient";
  private static final String EXAM_REQUEST = "examRequest";

  private ExamSpecifications() {
  }

  /**
   * Builds a specification that filters exams by the given filters.
   *
   * @param id the exam id filter
   * @param bed the bed filter
   * @param patientId the patient id filter
   * @param examRequestId the exam request id filter
   * @return the specification
   */
  public static Specification<Exam> withFilters(Long id, String bed,
      Long patientId, Long examRequestId) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (isNotBlank(bed)) {
        predicates.add(cb.like(root.get(BED), LIKE_WILDCARD + bed + LIKE_WILDCARD));
      }
      if (nonNull(id)) {
        predicates.add(cb.equal(root.get(ID), id));
      }
      if (nonNull(patientId)) {
        predicates.add(cb.equal(root.get(PATIENT).get(ID), patientId));
      }
      if (nonNull(examRequestId)) {
        predicates.add(cb.equal(root.get(EXAM_REQUEST).get(ID), examRequestId));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}