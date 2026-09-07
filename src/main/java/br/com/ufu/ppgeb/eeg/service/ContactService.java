package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Contact operations.
 */
public interface ContactService {

  /**
   * Saves a contact.
   *
   * @param customer the contact to save
   * @return the saved contact
   */
  Contact save(Contact customer);

  /**
   * Saves a list of contacts.
   *
   * @param contactList the contact list
   * @param objectType the object type
   * @param objectId the object id
   */
  void saveContactList(List<Contact> contactList,
      ObjectType objectType, Long objectId);

  /**
   * Finds a contact by id.
   *
   * @param id the contact id
   * @return the contact
   */
  Contact findById(Long id);

  /**
   * Finds contacts by filter.
   *
   * @param objectType the object type
   * @param objectId the object id
   * @param pageable the pagination information
   * @return the page of contacts
   */
  Page<Contact> findByFilter(Long objectType, Long objectId, Pageable pageable);

  /**
   * Finds all contacts.
   *
   * @return the list of contacts
   */
  List<Contact> findAll();

  /**
   * Deletes a contact by id.
   *
   * @param id the contact id
   */
  void delete(Long id);

  /**
   * Updates a contact.
   *
   * @param customer the contact to update
   * @return the updated contact
   */
  Contact update(Contact customer);

  /**
   * Validates a list of contacts.
   *
   * @param contactList the contact list to validate
   */
  void validateContactList(List<Contact> contactList);

}
