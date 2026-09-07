package br.com.ufu.ppgeb.eeg.repository;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Specifications for Contact queries.
 */
public final class ContactSpecifications {

  private static final String OBJECT_TYPE = "objectType";
  private static final String OBJECT_ID = "objectId";

  private ContactSpecifications() {
  }

  /**
   * Builds a specification that filters contacts by the given filters.
   *
   * @param objectType the object type filter
   * @param objectId the object id filter
   * @return the specification
   */
  public static Specification<Contact> withFilters(Long objectType, Long objectId) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (nonNull(objectType)) {
        predicates.add(cb.equal(root.get(OBJECT_TYPE), objectType));
      }
      if (nonNull(objectId)) {
        predicates.add(cb.equal(root.get(OBJECT_ID), objectId));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}