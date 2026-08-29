package br.com.ufu.ppgeb.eeg.controller;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATH_SEPARATOR;
import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATIENT;
import static org.instancio.Select.field;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import br.com.ufu.ppgeb.eeg.exception.GlobalExceptionHandler;
import br.com.ufu.ppgeb.eeg.exception.ResourceNotFoundException;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.service.PatientService;
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
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

  private static final Long ID = 1001L;
  private static final String NAME = "JOAO LUDOVICO";
  private static final String JSON_PATH_NAME = "$.name";
  private static final String JSON_PATH_ID = "$.id";
  private static final String MESSAGE = "name must not be blank";

  @Mock
  private PatientService patientService;

  private MockMvc mockMvc;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp() {
    LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
    validator.afterPropertiesSet();
    mockMvc = MockMvcBuilders.standaloneSetup(new PatientController(patientService))
        .setValidator(validator)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("Given existing patient ID when finding patient then return patient")
  void givenExistingPatientId_whenFindingPatient_thenReturnPatient() throws Exception {
    Patient patient = createPatient();
    when(patientService.findById(ID)).thenReturn(patient);

    mockMvc.perform(get(PATIENT + PATH_SEPARATOR + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME).value(NAME));

    verify(patientService).findById(ID);
  }

  @Test
  @DisplayName("Given unknown patient ID when finding patient then return not found")
  void givenUnknownPatientId_whenFindingPatient_thenReturnNotFound() throws Exception {
    when(patientService.findById(ID))
        .thenThrow(new ResourceNotFoundException("Paciente", ID));

    mockMvc.perform(get(PATIENT + PATH_SEPARATOR + ID))
        .andExpect(status().isNotFound());

    verify(patientService).findById(ID);
  }

  @Test
  @DisplayName("Given patient name when searching patients then return matching patient")
  void givenPatientName_whenSearchingPatients_thenReturnPatient() throws Exception {
    Patient patient = createPatient();
    when(patientService.findByFilter(NAME, null)).thenReturn(java.util.List.of(patient));

    mockMvc.perform(get(PATIENT).param("name", NAME))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name").value(NAME));

    verify(patientService).findByFilter(NAME, null);
  }

  @Test
  @DisplayName("Given valid patient when creating patient then return created with location")
  void givenValidPatient_whenCreatingPatient_thenReturnCreated() throws Exception {
    Patient saved = createPatient();
    when(patientService.save(any(Patient.class))).thenReturn(saved);

    String body = objectMapper.writeValueAsString(Instancio.create(PatientRequest.class));

    mockMvc.perform(post(PATIENT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(JSON_PATH_ID).value(ID))
        .andExpect(jsonPath(JSON_PATH_NAME).value(NAME))
        .andExpect(header().string("Location",
            PATIENT + PATH_SEPARATOR + ID));

    verify(patientService).save(any(Patient.class));
  }

  @Test
  @DisplayName("Given patient with blank name when creating patient then return bad request")
  void givenPatientWithBlankName_whenCreatingPatient_thenReturnBadRequest() throws Exception {
    PatientRequest request = Instancio.of(PatientRequest.class)
        .set(field(PatientRequest::name), null)
        .create();

    String body = objectMapper.writeValueAsString(request);

    mockMvc.perform(post(PATIENT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(MESSAGE));
  }

  @Test
  @DisplayName("Given existing patient when updating patient then return updated patient")
  void givenExistingPatient_whenUpdatingPatient_thenReturnUpdatedPatient() throws Exception {
    Patient updated = createPatient();
    when(patientService.update(any(Patient.class))).thenReturn(updated);

    String body = objectMapper.writeValueAsString(Instancio.create(PatientRequest.class));

    mockMvc.perform(put(PATIENT + PATH_SEPARATOR + ID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME).value(NAME));

    verify(patientService).update(any(Patient.class));
  }

  private Patient createPatient() {
    return Instancio.of(Patient.class)
        .set(field(Patient::getId), ID)
        .set(field(Patient::getName), NAME)
        .create();
  }
}
