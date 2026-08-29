package br.com.ufu.ppgeb.eeg.controller;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.constant.ApiPaths;
import br.com.ufu.ppgeb.eeg.dto.ContactRequest;
import br.com.ufu.ppgeb.eeg.dto.ContactResponse;
import br.com.ufu.ppgeb.eeg.mapper.ContactMapper;
import br.com.ufu.ppgeb.eeg.service.ContactService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  /**
   * Lists contacts with optional filters.
   *
   * @param objectType the object type filter
   * @param objectId the object id filter
   * @return the list of contacts
   */
  @GetMapping
  public List<ContactResponse> list(@RequestParam(value = "objectType",
          required = false) Long objectType,
      @RequestParam(value = "objectId",
          required = false) Long objectId) {

    logger.info("Consultando contatos; objectType={}, objectId={}", objectType, objectId);
    return Optional.ofNullable(contactService.findByFilter(objectType, objectId))
        .orElse(List.of())
        .stream()
        .map(ContactMapper::toResponse)
        .toList();
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
   * @param request the contact to save
   * @return the saved contact
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ContactResponse save(@Valid @RequestBody ContactRequest request) {

    logger.info("Recebendo criação de contato");
    return ContactMapper.toResponse(contactService.save(ContactMapper.toEntity(request)));
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
  @PutMapping("/{id}")
  public ContactResponse update(
      @PathVariable(value = "id") Long id,
      @Valid @RequestBody ContactRequest request) {

    logger.info("Recebendo atualização de contato id={}", id);
    return ContactMapper.toResponse(contactService.update(ContactMapper.toEntity(request, id)));
  }
}
