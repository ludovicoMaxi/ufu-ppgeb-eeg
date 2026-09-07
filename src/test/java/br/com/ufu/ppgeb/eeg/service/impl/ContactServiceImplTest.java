package br.com.ufu.ppgeb.eeg.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.model.ObjectType;
import br.com.ufu.ppgeb.eeg.repository.ContactRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {

  private static final String MSG_CONTACT_NULL = "contact cannot be null.";
  private static final String MSG_NAME_EMPTY = "name cannot be empty.";
  private static final String MSG_CELLPHONE_EMPTY = "cellphone cannot be empty.";
  private static final String MSG_ID_NULL = "id cannot be null.";
  private static final String MSG_CONTACT_LIST_EMPTY = "contactList cannot be empty.";
  private static final String MSG_OBJECT_TYPE_NULL = "objectType cannot be null.";
  private static final String MSG_OBJECT_ID_NULL = "objectId cannot be empty.";
  private static final String MSG_OBJECT_TYPE_FILTER = "ObjectType deve ser informado!";
  private static final String MSG_OBJECT_ID_FILTER = "ObjectId deve ser informado!";
  private static final String MSG_ONE_MAIN = "Allowed only one main.";
  private static final String MSG_MUST_HAVE_MAIN = "Must have a principal.";
  private static final String RESOURCE_NAME = "Contact";
  private static final String MSG_NOT_FOUND = " não encontrado(a) com id=";
  private static final String UPDATED_NAME = "Updated Name";
  private static final Long CONTACT_ID = 1L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final Long OBJECT_ID = 100L;
  private static final int TWO_CONTACTS = 2;
  private static final Pageable PAGEABLE = PageRequest.of(0, 10);

  @Mock
  private ContactRepository contactRepository;

  @InjectMocks
  private ContactServiceImpl contactService;

  @Test
  @DisplayName("Given two contacts in database when findAll then return all contacts")
  void givenTwoContactsInDatabase_whenFindAll_thenReturnAllContacts() {
    Contact contact1 = Instancio.create(Contact.class);
    Contact contact2 = Instancio.create(Contact.class);

    List<Contact> contacts = List.of(contact1, contact2);
    when(contactRepository.findAll()).thenReturn(contacts);

    List<Contact> result = contactService.findAll();

    assertThat(result).hasSize(TWO_CONTACTS);
    verify(contactRepository).findAll();
  }

  @Test
  @DisplayName("Given no contacts in database when findAll then return empty list")
  void givenNoContactsInDatabase_whenFindAll_thenReturnEmptyList() {
    when(contactRepository.findAll()).thenReturn(Collections.emptyList());

    List<Contact> result = contactService.findAll();

    assertThat(result).isEmpty();
    verify(contactRepository).findAll();
  }

  @Test
  @DisplayName("Given valid contact when save then return saved contact")
  void givenValidContact_whenSave_thenReturnSavedContact() {
    Contact contact = Instancio.create(Contact.class);

    when(contactRepository.save(any(Contact.class)))
        .thenReturn(contact);

    contactService.save(contact);

    verify(contactRepository).save(contact);
  }

  @Test
  @DisplayName("Given null contact when save then throw exception")
  void givenNullContact_whenSave_thenThrowException() {
    assertThatThrownBy(() -> contactService.save(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_CONTACT_NULL);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given contact with empty name when save then throw exception")
  void givenContactWithEmptyName_whenSave_thenThrowException() {
    Contact contact = Instancio.create(Contact.class);
    contact.setName("");

    assertThatThrownBy(() -> contactService.save(contact))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_NAME_EMPTY);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given contact with empty cellphone when save then throw exception")
  void givenContactWithEmptyCellphone_whenSave_thenThrowException() {
    Contact contact = Instancio.create(Contact.class);
    contact.setCellphone("");

    assertThatThrownBy(() -> contactService.save(contact))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_CELLPHONE_EMPTY);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given existing id when findById then return contact")
  void givenExistingId_whenFindById_thenReturnContact() {
    Contact contact = Instancio.create(Contact.class);
    contact.setId(CONTACT_ID);

    when(contactRepository.findById(CONTACT_ID)).thenReturn(Optional.of(contact));

    Contact result = contactService.findById(CONTACT_ID);

    assertThat(result.getId()).isEqualTo(CONTACT_ID);
    verify(contactRepository).findById(CONTACT_ID);
  }

  @Test
  @DisplayName("Given null id when findById then throw exception")
  void givenNullId_whenFindById_thenThrowException() {
    assertThatThrownBy(() -> contactService.findById(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(contactRepository, never()).findById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when findById then throw exception")
  void givenNonexistentId_whenFindById_thenThrowException() {
    when(contactRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> contactService.findById(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given both filter params null when findByFilter then return all contacts")
  void givenBothFilterParamsNull_whenFindByFilter_thenReturnAllContacts() {
    Contact contact = Instancio.create(Contact.class);
    when(contactRepository.findAll(PAGEABLE)).thenReturn(new PageImpl<>(List.of(contact)));

    Page<Contact> result = contactService.findByFilter(null, null, PAGEABLE);

    assertThat(result.getContent()).hasSize(1);
    verify(contactRepository).findAll(PAGEABLE);
  }

  @Test
  @DisplayName("Given null object type when findByFilter then throw exception")
  void givenNullObjectType_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> contactService.findByFilter(null, OBJECT_ID, PAGEABLE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_OBJECT_TYPE_FILTER);

    verify(contactRepository, never()).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  @DisplayName("Given null object id when findByFilter then throw exception")
  void givenNullObjectId_whenFindByFilter_thenThrowException() {
    assertThatThrownBy(() -> contactService.findByFilter(1L, null, PAGEABLE))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_OBJECT_ID_FILTER);

    verify(contactRepository, never()).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  @DisplayName("Given existing id when delete then remove contact")
  void givenExistingId_whenDelete_thenRemoveContact() {
    when(contactRepository.existsById(CONTACT_ID)).thenReturn(true);

    contactService.delete(CONTACT_ID);

    verify(contactRepository).existsById(CONTACT_ID);
    verify(contactRepository).deleteById(CONTACT_ID);
  }

  @Test
  @DisplayName("Given null id when delete then throw exception")
  void givenNullId_whenDelete_thenThrowException() {
    assertThatThrownBy(() -> contactService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(contactRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Given nonexistent id when delete then throw exception")
  void givenNonexistentId_whenDelete_thenThrowException() {
    when(contactRepository.existsById(NONEXISTENT_ID)).thenReturn(false);

    assertThatThrownBy(() -> contactService.delete(NONEXISTENT_ID))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(RESOURCE_NAME + MSG_NOT_FOUND + NONEXISTENT_ID);

    verify(contactRepository, never()).deleteById(NONEXISTENT_ID);
  }

  @Test
  @DisplayName("Given valid contact when update then return updated contact")
  void givenValidContact_whenUpdate_thenReturnUpdatedContact() {
    Contact oldContact = createContact(CONTACT_ID, "Old Name");
    Contact newContact = createContact(CONTACT_ID, UPDATED_NAME);

    when(contactRepository.findById(CONTACT_ID)).thenReturn(Optional.of(oldContact));
    when(contactRepository.save(any(Contact.class)))
        .thenReturn(oldContact);

    Contact result = contactService.update(newContact);

    assertThat(result.getName()).isEqualTo(UPDATED_NAME);
    verify(contactRepository).findById(CONTACT_ID);
    verify(contactRepository).save(oldContact);
  }

  private Contact createContact(Long id, String name) {
    Contact contact = Instancio.create(Contact.class);
    contact.setId(id);
    contact.setName(name);
    return contact;
  }

  @Test
  @DisplayName("Given contact with null id when update then throw exception")
  void givenContactWithNullId_whenUpdate_thenThrowException() {
    Contact contact = Instancio.create(Contact.class);
    contact.setId(null);

    assertThatThrownBy(() -> contactService.update(contact))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ID_NULL);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given contact list with one main when validateContactList then pass")
  void givenContactListWithOneMain_whenValidateContactList_thenPass() {
    Contact contact1 = Instancio.create(Contact.class);
    contact1.setMain(true);
    Contact contact2 = Instancio.create(Contact.class);
    contact2.setMain(false);

    contactService.validateContactList(List.of(contact1, contact2));

  }

  @Test
  @DisplayName("Given contact list with two mains when validateContactList then throw exception")
  void givenContactListWithTwoMains_whenValidateContactList_thenThrowException() {
    Contact contact1 = Instancio.create(Contact.class);
    contact1.setMain(true);
    Contact contact2 = Instancio.create(Contact.class);
    contact2.setMain(true);

    assertThatThrownBy(() -> contactService.validateContactList(List.of(contact1, contact2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_ONE_MAIN);
  }

  @Test
  @DisplayName("Given contact list with no main when validateContactList then throw exception")
  void givenContactListWithNoMain_whenValidateContactList_thenThrowException() {
    Contact contact1 = Instancio.create(Contact.class);
    contact1.setMain(false);
    Contact contact2 = Instancio.create(Contact.class);
    contact2.setMain(false);

    assertThatThrownBy(() -> contactService.validateContactList(List.of(contact1, contact2)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_MUST_HAVE_MAIN);
  }

  @Test
  @DisplayName("Given empty contact list when validateContactList then throw exception")
  void givenEmptyContactList_whenValidateContactList_thenThrowException() {
    assertThatThrownBy(() -> contactService.validateContactList(Collections.emptyList()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_CONTACT_LIST_EMPTY);
  }

  @Test
  @DisplayName("Given valid contact list when saveContactList then save all contacts")
  void givenValidContactList_whenSaveContactList_thenSaveAllContacts() {
    Contact contact = Instancio.create(Contact.class);
    ObjectType objectType = new ObjectType(1L);

    when(contactRepository.save(any(Contact.class)))
        .thenReturn(contact);

    contactService.saveContactList(List.of(contact), objectType, OBJECT_ID);

    verify(contactRepository).save(contact);
  }

  @Test
  @DisplayName("Given empty contact list when saveContactList then throw exception")
  void givenEmptyContactList_whenSaveContactList_thenThrowException() {
    ObjectType objectType = new ObjectType(1L);

    assertThatThrownBy(() -> contactService.saveContactList(Collections.emptyList(), objectType, OBJECT_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_CONTACT_LIST_EMPTY);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null object type when saveContactList then throw exception")
  void givenNullObjectType_whenSaveContactList_thenThrowException() {
    Contact contact = Instancio.create(Contact.class);

    assertThatThrownBy(() -> contactService.saveContactList(List.of(contact), null, OBJECT_ID))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_OBJECT_TYPE_NULL);

    verify(contactRepository, never()).save(any());
  }

  @Test
  @DisplayName("Given null object id when saveContactList then throw exception")
  void givenNullObjectId_whenSaveContactList_thenThrowException() {
    Contact contact = Instancio.create(Contact.class);
    ObjectType objectType = new ObjectType(1L);

    assertThatThrownBy(() -> contactService.saveContactList(List.of(contact), objectType, null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(MSG_OBJECT_ID_NULL);

    verify(contactRepository, never()).save(any());
  }
}
