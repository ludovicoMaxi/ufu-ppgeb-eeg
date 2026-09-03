package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.CONTACT;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import br.com.ufu.ppgeb.eeg.dto.ContactRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Contact;
import br.com.ufu.ppgeb.eeg.service.ContactService;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {

  private static final Long CONTACT_ID = 1001L;
  private static final Long OBJECT_TYPE = 1L;
  private static final String CONTACT_NAME = "MARIA";
  private static final String JSON_PATH_NAME = "$.name";
  private static final String JSON_PATH_LENGTH = "$.length()";

  @Mock
  private ContactService contactService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(new ContactController(contactService))
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given contacts in service when listing contacts then return contacts")
  void givenContactsInService_whenListingContacts_thenReturnContacts() throws Exception {
    Contact contact = createContact();
    when(contactService.findByFilter(OBJECT_TYPE, CONTACT_ID)).thenReturn(List.of(contact));

    mockMvc.perform(get(CONTACT)
            .param("objectType", String.valueOf(OBJECT_TYPE))
            .param("objectId", String.valueOf(CONTACT_ID)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$[0].name").value(CONTACT_NAME));

    verify(contactService).findByFilter(OBJECT_TYPE, CONTACT_ID);
  }

  @Test
  @DisplayName("Given existing contact ID when finding contact then return contact")
  void givenExistingContactId_whenFindingContact_thenReturnContact() throws Exception {
    Contact contact = createContact();
    when(contactService.findById(CONTACT_ID)).thenReturn(contact);

    mockMvc.perform(get(CONTACT + PATH_SEPARATOR + CONTACT_ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME).value(CONTACT_NAME));

    verify(contactService).findById(CONTACT_ID);
  }

  @Test
  @DisplayName("Given unknown contact ID when finding contact then return not found")
  void givenUnknownContactId_whenFindingContact_thenReturnNotFound() throws Exception {
    when(contactService.findById(CONTACT_ID))
        .thenThrow(new ResourceNotFoundException("Contato", CONTACT_ID));

    mockMvc.perform(get(CONTACT + PATH_SEPARATOR + CONTACT_ID))
        .andExpect(status().isNotFound());

    verify(contactService).findById(CONTACT_ID);
  }

  @Test
  @DisplayName("Given a contact when saving contact then return created contact")
  void givenContact_whenSavingContact_thenReturnCreatedContact() throws Exception {
    Contact contact = createContact();
    when(contactService.save(any(Contact.class))).thenReturn(contact);

    String body = objectMapper.writeValueAsString(Instancio.create(ContactRequest.class));

    mockMvc.perform(post(CONTACT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            CONTACT + PATH_SEPARATOR + CONTACT_ID))
        .andExpect(jsonPath(JSON_PATH_NAME).value(CONTACT_NAME));

    verify(contactService).save(any(Contact.class));
  }

  @Test
  @DisplayName("Given a contact id when deleting contact then return no content")
  void givenContactId_whenDeletingContact_thenReturnNoContent() throws Exception {
    mockMvc.perform(delete(CONTACT + PATH_SEPARATOR + CONTACT_ID))
        .andExpect(status().isNoContent());

    verify(contactService).delete(CONTACT_ID);
  }

  @Test
  @DisplayName("Given a contact when updating contact then return updated contact")
  void givenContact_whenUpdatingContact_thenReturnUpdatedContact() throws Exception {
    Contact contact = createContact();
    when(contactService.update(any(Contact.class))).thenReturn(contact);

    String body = objectMapper.writeValueAsString(Instancio.create(ContactRequest.class));

    mockMvc.perform(put(CONTACT + PATH_SEPARATOR + CONTACT_ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME).value(CONTACT_NAME));

    verify(contactService).update(any(Contact.class));
  }

  private Contact createContact() {
    return Instancio.of(Contact.class)
        .set(field(Contact::getId), CONTACT_ID)
        .set(field(Contact::getName), CONTACT_NAME)
        .create();
  }
}
