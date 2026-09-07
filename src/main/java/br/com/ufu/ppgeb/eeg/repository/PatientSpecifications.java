package br.com.ufu.ppgeb.eeg.repository;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for Patient queries.
 */
public final class PatientSpecifications {

  private static final String LIKE_WILDCARD = "%";
  private static final String NAME = "name";
  private static final String DOCUMENT_NUMBER = "documentNumber";

  private PatientSpecifications() {
  }

  /**
   * Builds a specification that filters patients by the given filters.
   *
   * @param name the name filter
   * @param documentNumber the document number filter
   * @return the specification
   */
  public static Specification<Patient> withFilters(String name, String documentNumber) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (isNotBlank(name)) {
        predicates.add(cb.like(root.get(NAME), LIKE_WILDCARD + name + LIKE_WILDCARD));
      }
      if (isNotBlank(documentNumber)) {
        predicates.add(cb.equal(root.get(DOCUMENT_NUMBER), documentNumber));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}