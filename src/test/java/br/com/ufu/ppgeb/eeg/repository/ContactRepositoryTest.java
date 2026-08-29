package br.com.ufu.ppgeb.eeg.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.config.AuditingConfig;
import br.com.ufu.ppgeb.eeg.model.Contact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(AuditingConfig.class)
class ContactRepositoryTest {

  private static final Long OBJECT_TYPE_100 = 100L;
  private static final Long OBJECT_TYPE_200 = 200L;
  private static final Long OBJECT_ID_1 = 1L;
  private static final Long OBJECT_ID_2 = 2L;
  private static final Long NONEXISTENT_ID = 999L;
  private static final String NAME_1 = "Contato 1";
  private static final String NAME_2 = "Contato 2";
  private static final String PHONE_1 = "9999-9999";
  private static final String USERNAME = "joaol";
  private static final int TWO_CONTACTS = 2;

  @Autowired
  private ContactRepository contactRepository;

  @Autowired
  private TestEntityManager testEntityManager;

  @BeforeEach
  void setUpAuthentication() {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, "123",
        List.of(new SimpleGrantedAuthority("ROLE_USER"))));
    SecurityContextHolder.setContext(context);
  }

  @AfterEach
  void tearDownAuthentication() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Given valid contact when save then generate id and fill auditing fields")
  void givenValidContact_whenSave_thenGenerateIdAndFillAuditingFields() {
    Contact contact = createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1);
    assertThat(contact.getId()).isNull();

    Contact saved = contactRepository.save(contact);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getName()).isEqualTo(NAME_1);
    assertThat(saved.getObjectType()).isEqualTo(OBJECT_TYPE_100);
    assertThat(saved.getObjectId()).isEqualTo(OBJECT_ID_1);
    assertThat(saved.getCreatedAt()).isNotNull();
    assertThat(saved.getCreatedBy()).isEqualTo(USERNAME);
    assertThat(saved.getUpdatedAt()).isNull();
    assertThat(saved.getUpdatedBy()).isNull();
  }

  @Test
  @DisplayName("Given existing contact when update then fill updated auditing fields and preserve creation fields")
  void givenExistingContact_whenUpdate_thenFillUpdatedAuditingFieldsAndPreserveCreationFields() {
    Contact saved = contactRepository.save(
        createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));

    saved.setName(NAME_2);
    contactRepository.save(saved);
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<Contact> result = contactRepository.findById(saved.getId());

    assertThat(result.get().getName()).isEqualTo(NAME_2);
    assertThat(result.get().getUpdatedAt()).isNotNull();
    assertThat(result.get().getUpdatedBy()).isEqualTo(USERNAME);
    assertThat(result.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    assertThat(result.get().getCreatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given detached contact with existing id when save then merge updates existing row")
  void givenDetachedContactWithExistingId_whenSave_thenMergeUpdatesExistingRowAndPreservesCreationAudit() {
    Contact saved = contactRepository.save(
        createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    saved.setPhone(PHONE_1);
    final ZonedDateTime originalCreatedAt = saved.getCreatedAt().truncatedTo(ChronoUnit.MILLIS);
    testEntityManager.flush();
    testEntityManager.clear();

    Contact detached = createContact(OBJECT_TYPE_200, OBJECT_ID_2, NAME_2);
    detached.setId(saved.getId());

    Contact merged = contactRepository.save(detached);
    testEntityManager.flush();
    testEntityManager.clear();

    Optional<Contact> result = contactRepository.findById(saved.getId());

    assertThat(merged).isNotSameAs(detached);
    assertThat(contactRepository.findAll()).hasSize(1);
    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_2);
    assertThat(result.get().getObjectType()).isEqualTo(OBJECT_TYPE_200);
    assertThat(result.get().getObjectId()).isEqualTo(OBJECT_ID_2);
    assertThat(result.get().getPhone()).isNull();
    assertThat(result.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS))
        .isEqualTo(originalCreatedAt);
    assertThat(result.get().getCreatedBy()).isEqualTo(USERNAME);
    assertThat(result.get().getUpdatedAt()).isNotNull();
    assertThat(result.get().getUpdatedBy()).isEqualTo(USERNAME);
  }

  @Test
  @DisplayName("Given saved contact when findById then return the contact")
  void givenSavedContact_whenFindById_thenReturnTheContact() {
    Contact saved = contactRepository.save(
        createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));

    Optional<Contact> result = contactRepository.findById(saved.getId());

    assertThat(result).isPresent();
    assertThat(result.get().getName()).isEqualTo(NAME_1);
  }

  @Test
  @DisplayName("Given two saved contacts when findAll then return all contacts")
  void givenTwoSavedContacts_whenFindAll_thenReturnAllContacts() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_2, NAME_2));

    List<Contact> result = contactRepository.findAll();

    assertThat(result).hasSize(TWO_CONTACTS);
  }

  @Test
  @DisplayName("Given contacts with distinct types when findByFilter objectType then return only matches")
  void givenContactsWithDistinctTypes_whenFindByFilterObjectType_thenReturnOnlyMatches() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    contactRepository.save(createContact(OBJECT_TYPE_200, OBJECT_ID_2, NAME_2));

    List<Contact> result = contactRepository.findByFilter(OBJECT_TYPE_100, null);

    assertThat(result).extracting(Contact::getName).containsExactly(NAME_1);
  }

  @Test
  @DisplayName("Given contacts with distinct objects when findByFilter objectId then return only matches")
  void givenContactsWithDistinctObjects_whenFindByFilterObjectId_thenReturnOnlyMatches() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_2, NAME_2));

    List<Contact> result = contactRepository.findByFilter(null, OBJECT_ID_2);

    assertThat(result).extracting(Contact::getName).containsExactly(NAME_2);
  }

  @Test
  @DisplayName("Given contacts when findByFilter objectType and objectId then return only matches")
  void givenContacts_whenFindByFilterObjectTypeAndObjectId_thenReturnOnlyMatches() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_2, NAME_2));
    contactRepository.save(createContact(OBJECT_TYPE_200, OBJECT_ID_2, NAME_2));

    List<Contact> result = contactRepository.findByFilter(OBJECT_TYPE_100, OBJECT_ID_2);

    assertThat(result).extracting(Contact::getName).containsExactly(NAME_2);
  }

  @Test
  @DisplayName("Given saved contacts when findByFilter without filters then return all contacts")
  void givenSavedContacts_whenFindByFilterWithoutFilters_thenReturnAllContacts() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));
    contactRepository.save(createContact(OBJECT_TYPE_200, OBJECT_ID_2, NAME_2));

    List<Contact> result = contactRepository.findByFilter(null, null);

    assertThat(result).hasSize(TWO_CONTACTS);
  }

  @Test
  @DisplayName("Given no matching contacts when findByFilter then return empty list")
  void givenNoMatchingContacts_whenFindByFilter_thenReturnEmptyList() {
    contactRepository.save(createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));

    List<Contact> result = contactRepository.findByFilter(OBJECT_TYPE_200, null);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("Given existing id when existsById then return true")
  void givenExistingId_whenExistsById_thenReturnTrue() {
    Contact saved = contactRepository.save(
        createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));

    boolean result = contactRepository.existsById(saved.getId());

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("Given nonexistent id when existsById then return false")
  void givenNonexistentId_whenExistsById_thenReturnFalse() {
    boolean result = contactRepository.existsById(NONEXISTENT_ID);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Given saved contact when deleteById then contact is removed")
  void givenSavedContact_whenDeleteById_thenContactIsRemoved() {
    Contact saved = contactRepository.save(
        createContact(OBJECT_TYPE_100, OBJECT_ID_1, NAME_1));

    contactRepository.deleteById(saved.getId());

    assertThat(contactRepository.existsById(saved.getId())).isFalse();
    assertThat(contactRepository.findAll()).isEmpty();
  }

  private Contact createContact(Long objectType, Long objectId, String name) {
    return Contact.builder()
        .name(name)
        .active(true)
        .objectId(objectId)
        .objectType(objectType)
        .build();
  }
}
