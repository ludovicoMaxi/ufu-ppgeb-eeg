package br.com.ufu.ppgeb.eeg.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import br.com.ufu.ppgeb.eeg.dto.ContactRequest;
import br.com.ufu.ppgeb.eeg.dto.ContactResponse;
import br.com.ufu.ppgeb.eeg.model.Contact;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContactMapperTest {

  private static final Long ID = 1001L;
  private static final String ID_FIELD = "id";
  private static final String CREATED_AT_FIELD = "createdAt";
  private static final String CREATED_BY_FIELD = "createdBy";
  private static final String UPDATED_AT_FIELD = "updatedAt";
  private static final String UPDATED_BY_FIELD = "updatedBy";

  @Test
  @DisplayName("Given a contact request when mapping to entity then map every field keeping id null")
  void givenContactRequest_whenToEntity_thenMapEveryFieldKeepingIdNull() {
    ContactRequest request = Instancio.create(ContactRequest.class);

    Contact contact = ContactMapper.toEntity(request);

    assertThat(contact)
        .hasNoNullFieldsOrPropertiesExcept(ID_FIELD, CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(contact.getId()).isNull();
    assertThat(contact.getName()).isEqualTo(request.name());
    assertThat(contact.getActive()).isEqualTo(request.active());
    assertThat(contact.getObjectId()).isEqualTo(request.objectId());
    assertThat(contact.getObjectType()).isEqualTo(request.objectType());
    assertThat(contact.getPhone()).isEqualTo(request.phone());
    assertThat(contact.getCellphone()).isEqualTo(request.cellphone());
    assertThat(contact.getWhatsapp()).isEqualTo(request.whatsapp());
    assertThat(contact.getFacebook()).isEqualTo(request.facebook());
    assertThat(contact.getInstagram()).isEqualTo(request.instagram());
    assertThat(contact.getEmail()).isEqualTo(request.email());
    assertThat(contact.getMain()).isEqualTo(request.main());
  }

  @Test
  @DisplayName("Given a contact request and an id when mapping to entity then map every field with the id")
  void givenContactRequestAndId_whenToEntity_thenMapEveryFieldWithTheId() {
    ContactRequest request = Instancio.create(ContactRequest.class);

    Contact contact = ContactMapper.toEntity(request, ID);

    assertThat(contact)
        .hasNoNullFieldsOrPropertiesExcept(CREATED_AT_FIELD, CREATED_BY_FIELD,
            UPDATED_AT_FIELD, UPDATED_BY_FIELD);
    assertThat(contact.getId()).isEqualTo(ID);
    assertThat(contact.getName()).isEqualTo(request.name());
    assertThat(contact.getActive()).isEqualTo(request.active());
    assertThat(contact.getObjectId()).isEqualTo(request.objectId());
    assertThat(contact.getObjectType()).isEqualTo(request.objectType());
    assertThat(contact.getPhone()).isEqualTo(request.phone());
    assertThat(contact.getCellphone()).isEqualTo(request.cellphone());
    assertThat(contact.getWhatsapp()).isEqualTo(request.whatsapp());
    assertThat(contact.getFacebook()).isEqualTo(request.facebook());
    assertThat(contact.getInstagram()).isEqualTo(request.instagram());
    assertThat(contact.getEmail()).isEqualTo(request.email());
    assertThat(contact.getMain()).isEqualTo(request.main());
  }

  @Test
  @DisplayName("Given a null contact request when mapping to entity then return null")
  void givenNullContactRequest_whenToEntity_thenReturnNull() {

    Contact contact = ContactMapper.toEntity((ContactRequest) null);

    assertThat(contact).isNull();
  }

  @Test
  @DisplayName("Given a contact when mapping to response then map every field")
  void givenContact_whenToResponse_thenMapEveryField() {
    Contact contact = Instancio.create(Contact.class);

    ContactResponse response = ContactMapper.toResponse(contact);

    assertThat(response).hasNoNullFieldsOrProperties();
    assertThat(response.id()).isEqualTo(contact.getId());
    assertThat(response.name()).isEqualTo(contact.getName());
    assertThat(response.active()).isEqualTo(contact.getActive());
    assertThat(response.objectId()).isEqualTo(contact.getObjectId());
    assertThat(response.objectType()).isEqualTo(contact.getObjectType());
    assertThat(response.phone()).isEqualTo(contact.getPhone());
    assertThat(response.cellphone()).isEqualTo(contact.getCellphone());
    assertThat(response.whatsapp()).isEqualTo(contact.getWhatsapp());
    assertThat(response.facebook()).isEqualTo(contact.getFacebook());
    assertThat(response.instagram()).isEqualTo(contact.getInstagram());
    assertThat(response.email()).isEqualTo(contact.getEmail());
    assertThat(response.main()).isEqualTo(contact.getMain());
    assertThat(response.createdAt()).isEqualTo(contact.getCreatedAt());
    assertThat(response.createdBy()).isEqualTo(contact.getCreatedBy());
    assertThat(response.updatedAt()).isEqualTo(contact.getUpdatedAt());
    assertThat(response.updatedBy()).isEqualTo(contact.getUpdatedBy());
  }

  @Test
  @DisplayName("Given a null contact when mapping to response then return null")
  void givenNullContact_whenToResponse_thenReturnNull() {

    ContactResponse response = ContactMapper.toResponse(null);

    assertThat(response).isNull();
  }
}
