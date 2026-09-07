package br.com.ufu.ppgeb.eeg.controller;

import java.net.URI;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ContactRequest;
import br.com.ufu.ppgeb.eeg.dto.ContactResponse;
import br.com.ufu.ppgeb.eeg.mapper.ContactMapper;
import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.service.ContactService;
import br.com.ufu.ppgeb.eeg.service.IdempotencyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for contact operations.
 */
@RestController
@RequestMapping(ApiPaths.CONTACT)
@AllArgsConstructor
public class ContactController {

  private static final Logger logger = LoggerFactory.getLogger(ContactController.class);

  private final ContactService contactService;
  private final IdempotencyService idempotencyService;

  /**
   * Lists contacts with optional filters.
   *
   * @param objectType the object type filter
   * @param objectId the object id filter
   * @param pageable the pagination information
   * @return the page of contacts
   */
  @GetMapping
  public Page<ContactResponse> list(@RequestParam(value = "objectType",
          required = false) Long objectType,
      @RequestParam(value = "objectId",
          required = false) Long objectId,
      Pageable pageable) {

    logger.info("Consultando contatos; objectType={}, objectId={}", objectType, objectId);
    return contactService.findByFilter(objectType, objectId, pageable)
        .map(ContactMapper::toResponse);
  }

  /**
   * Finds a contact by id.
   *
   * @param id the contact id
   * @return the contact
   */
  @GetMapping("/{id}")
  public ContactResponse findById(@PathVariable(value = "id") Long id) {

    logger.info("Consultando contato id={}", id);
    return ContactMapper.toResponse(contactService.findById(id));
  }

  /**
   * Saves a new contact.
   *
   * @param idempotencyKey the idempotency key
   * @param request the contact to save
   * @return the saved contact
   */
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ContactResponse> save(
      @RequestHeader(name = IdempotencyService.IDEMPOTENCY_KEY_HEADER,
          required = false) String idempotencyKey,
      @Valid @RequestBody ContactRequest request) {

    logger.info("Recebendo criação de contato");
    return idempotencyService.execute(
        "CONTACT", idempotencyKey, ContactResponse.class,
        () -> {
          Contact saved = contactService.save(ContactMapper.toDomain(request));
          URI location = URI.create(
              ApiPaths.CONTACT + ApiPaths.PATH_SEPARATOR + saved.getId());
          return ResponseEntity.created(location)
              .body(ContactMapper.toResponse(saved));
        });
  }

  /**
   * Deletes a contact by id.
   *
   * @param id the contact id
   */
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable(value = "id") Long id) {

    logger.info("Recebendo remoção de contato id={}", id);
    contactService.delete(id);
  }

  /**
   * Updates a contact.
   *
   * @param id the contact id
   * @param request the contact to update
   * @return the updated contact
   */
  @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ContactResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ContactRequest request) {

    logger.info("Recebendo atualização de contato id={}", id);
    return ContactMapper.toResponse(contactService.update(ContactMapper.toDomain(request, id)));
  }
}
