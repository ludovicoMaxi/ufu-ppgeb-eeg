package br.com.ufu.ppgeb.eeg.mapper;

import static java.util.Objects.isNull;

import br.com.ufu.ppgeb.eeg.dto.ContactRequest;
import br.com.ufu.ppgeb.eeg.dto.ContactResponse;
import br.com.ufu.ppgeb.eeg.model.Contact;
import lombok.experimental.UtilityClass;

/**
 * Maps between the Contact entity and its request/response DTOs.
 */
@UtilityClass
public class ContactMapper {

  /**
   * Maps a create request to a Contact entity.
   *
   * @param request the create request
   * @return the Contact entity
   */
  public static Contact toEntity(ContactRequest request) {

    return toEntity(request, null);
  }

  /**
   * Maps an update request to a Contact entity preserving the id.
   *
   * @param request the update request
   * @param id the contact id
   * @return the Contact entity
   */
  public static Contact toEntity(ContactRequest request, Long id) {

    if (isNull(request)) {
      return null;
    }
    return Contact.builder()
        .id(id)
        .name(request.name())
        .active(request.active())
        .objectId(request.objectId())
        .objectType(request.objectType())
        .phone(request.phone())
        .cellphone(request.cellphone())
        .whatsapp(request.whatsapp())
        .facebook(request.facebook())
        .instagram(request.instagram())
        .email(request.email())
        .main(request.main())
        .build();
  }

  /**
   * Maps a Contact entity to a response DTO.
   *
   * @param contact the Contact entity
   * @return the response DTO
   */
  public static ContactResponse toResponse(Contact contact) {

    if (isNull(contact)) {
      return null;
    }
    return ContactResponse.builder()
        .id(contact.getId())
        .name(contact.getName())
        .active(contact.getActive())
        .objectId(contact.getObjectId())
        .objectType(contact.getObjectType())
        .phone(contact.getPhone())
        .cellphone(contact.getCellphone())
        .whatsapp(contact.getWhatsapp())
        .facebook(contact.getFacebook())
        .instagram(contact.getInstagram())
        .email(contact.getEmail())
        .main(contact.getMain())
        .createdAt(contact.getCreatedAt())
        .createdBy(contact.getCreatedBy())
        .updatedAt(contact.getUpdatedAt())
        .updatedBy(contact.getUpdatedBy())
        .build();
  }
}
