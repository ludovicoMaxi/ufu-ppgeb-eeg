package br.com.ufu.ppgeb.eeg.service.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.repository.ContactRepository;
import br.com.ufu.ppgeb.eeg.service.ContactService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementation of ContactService.
 */
@Service
@AllArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {

  private static final String MSG_CONTACT_LIST_EMPTY = "contactList cannot be empty.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String RESOURCE_NAME = "Contact";
  private static final String MSG_CONTACT_CREATED = "Contato criado com id={}";

  private final ContactRepository contactRepository;

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Contact save(Contact contact) {

    validateContact(contact);

    Contact saved = contactRepository.save(contact);
    log.info(MSG_CONTACT_CREATED, saved.getId());
    return saved;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveContactList(List<Contact> contactList,
      ObjectType objectType, Long objectId) {

    Assert.notEmpty(contactList, MSG_CONTACT_LIST_EMPTY);
    Assert.notNull(objectType, "objectType cannot be null.");
    Assert.notNull(objectId, "objectId cannot be empty.");

    for (Contact contact : contactList) {
      contact.setObjectId(objectId);
      contact.setObjectType(objectType.getId());
      validateContact(contact);
      Contact saved = contactRepository.save(contact);
      log.info(MSG_CONTACT_CREATED, saved.getId());
    }
    log.info("Lista de contatos salva; objectType={}, objectId={}, quantidade={}",
        objectType.getId(), objectId, contactList.size());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Contact> findAll() {

    return contactRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Contact findById(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    return contactRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, id));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Contact> findByFilter(Long objectType, Long objectId, Pageable pageable) {

    if (isNull(objectType) && isNull(objectId)) {
      return contactRepository.findAll(pageable);
    }

    validateSearchContact(objectType, objectId);
    return contactRepository.findByFilter(objectType, objectId, pageable);
  }

  private void validateSearchContact(Long objectType, Long objectId) {

    if (isNull(objectType)) {
      throw new IllegalArgumentException("ObjectType deve ser informado!");
    } else if (isNull(objectId)) {
      throw new IllegalArgumentException("ObjectId deve ser informado!");
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delete(Long id) {

    Assert.notNull(id, MSG_ID_NULL);
    if (!contactRepository.existsById(id)) {
      throw new ResourceNotFoundException(RESOURCE_NAME, id);
    }
    contactRepository.deleteById(id);
    log.info("Contato removido com id={}", id);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Contact update(Contact contact) {

    validateContact(contact);
    Assert.notNull(contact.getId(), MSG_ID_NULL);

    Contact oldContact = contactRepository.findById(contact.getId())
        .orElseThrow(() -> new ResourceNotFoundException(RESOURCE_NAME, contact.getId()));

    oldContact.setName(contact.getName());
    oldContact.setActive(contact.getActive());
    oldContact.setCellphone(contact.getCellphone());
    oldContact.setEmail(contact.getEmail());
    oldContact.setFacebook(contact.getFacebook());
    oldContact.setInstagram(contact.getInstagram());
    oldContact.setMain(contact.getMain());
    oldContact.setPhone(contact.getPhone());
    oldContact.setWhatsapp(contact.getWhatsapp());

    Contact updated = contactRepository.save(oldContact);
    log.info("Contato atualizado com id={}", updated.getId());
    return updated;
  }

  @Override
  public void validateContactList(List<Contact> contactList) {

    Assert.notEmpty(contactList, MSG_CONTACT_LIST_EMPTY);

    boolean hasMain = false;
    for (Contact contact : contactList) {
      validateContact(contact);
      if (nonNull(contact.getMain())
          && contact.getMain()) {
        if (hasMain) {
          throw new IllegalArgumentException("Allowed only one main.");
        }
        hasMain = true;
      }
    }

    if (!hasMain) {
      throw new IllegalArgumentException("Must have a principal.");
    }
  }

  private void validateContact(Contact contact) {

    Assert.notNull(contact, "contact cannot be null.");
    Assert.hasText(contact.getName(), "name cannot be empty.");
    Assert.hasText(contact.getCellphone(), "cellphone cannot be empty.");
  }
}
