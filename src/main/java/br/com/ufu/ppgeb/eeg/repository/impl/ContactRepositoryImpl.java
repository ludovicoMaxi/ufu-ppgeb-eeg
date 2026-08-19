package br.com.ufu.ppgeb.eeg.repository.impl;

import static java.util.Objects.nonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.repository.ContactRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 * Custom repository implementation for Contact queries.
 */
@Repository
public class ContactRepositoryImpl implements ContactRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<Contact> findByFilter(Long objectType, Long objectId) {

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Contact> cq = cb.createQuery(Contact.class);
    Root<Contact> root = cq.from(Contact.class);

    List<Predicate> predicates = new ArrayList<>();
    if (nonNull(objectType)) {
      predicates.add(cb.equal(root.get("objectType"), objectType));
    }
    if (nonNull(objectId)) {
      predicates.add(cb.equal(root.get("objectId"), objectId));
    }

    cq.where(predicates.toArray(new Predicate[0]));

    return em.createQuery(cq).getResultList();
  }
}
