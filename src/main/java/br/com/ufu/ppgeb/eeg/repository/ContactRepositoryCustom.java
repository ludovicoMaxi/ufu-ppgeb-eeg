package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;

/**
 * Custom repository for Contact queries.
 */
public interface ContactRepositoryCustom {

  /**
   * Finds contacts by filter.
   *
   * @param objectType the object type
   * @param objectId the object id
   * @return the list of contacts
   */
  List<Contact> findByFilter(Long objectType, Long objectId);

}
