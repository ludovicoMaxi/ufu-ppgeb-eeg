package br.com.ufu.ppgeb.eeg.repository.impl;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.repository.PatientRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Custom repository implementation for Patient queries.
 */
@Repository
public class PatientRepositoryImpl implements PatientRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Patient> findByFilter(String name, String documentNumber) {

    if (StringUtils.isBlank(name) && StringUtils.isBlank(documentNumber)) {
      throw new IllegalArgumentException("Informe pelo menos um campo para consultar!");
    }

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Patient> cq = cb.createQuery(Patient.class);
    Root<Patient> root = cq.from(Patient.class);

    List<Predicate> predicates = new ArrayList<>();
    if (StringUtils.isNotBlank(name)) {
      predicates.add(cb.like(root.get("name"), "%" + name + "%"));
    }
    if (StringUtils.isNotBlank(documentNumber)) {
      predicates.add(cb.equal(root.get("documentNumber"), documentNumber));
    }

    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).getResultList();
  }
}
