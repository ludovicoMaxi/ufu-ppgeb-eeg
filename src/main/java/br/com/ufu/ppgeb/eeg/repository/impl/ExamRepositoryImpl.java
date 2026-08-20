package br.com.ufu.ppgeb.eeg.repository.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Exam;
import br.com.ufu.ppgeb.eeg.repository.ExamRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Custom repository implementation for Exam queries.
 */
@Repository
public class ExamRepositoryImpl implements ExamRepositoryCustom {

  private static final String LIKE_WILDCARD = "%";
  private static final String ID = "id";

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Exam> findByFilter(Long id, String bed,
      Long patientId, Long examRequestId) {

    if (isNull(id)
        && StringUtils.isBlank(bed)
        && isNull(patientId)
        && isNull(examRequestId)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Exam> cq = cb.createQuery(Exam.class);
    Root<Exam> root = cq.from(Exam.class);

    List<Predicate> predicates = new ArrayList<>();
    if (StringUtils.isNotBlank(bed)) {
      predicates.add(cb.like(root.get("bed"), LIKE_WILDCARD + bed + LIKE_WILDCARD));
    }
    if (nonNull(id)) {
      predicates.add(cb.equal(root.get(ID), id));
    }
    if (nonNull(patientId)) {
      predicates.add(cb.equal(root.get("patient").get(ID), patientId));
    }
    if (nonNull(examRequestId)) {
      predicates.add(cb.equal(root.get("examRequest").get(ID), examRequestId));
    }

    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).getResultList();
  }
}
