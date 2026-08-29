package br.com.ufu.ppgeb.eeg;

import static br.com.ufu.ppgeb.eeg.constant.ApiPaths.PATIENT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import br.com.ufu.ppgeb.eeg.model.CivilStatus;
import br.com.ufu.ppgeb.eeg.model.Patient;
import br.com.ufu.ppgeb.eeg.model.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
@ActiveProfiles("test")
class ApiIntegrationTest {

  private static final String USERNAME = "joaol";
  private static final String PASSWORD = "123";
  private static final String JSON_PATH_NAME = "$.name";
  private static final String NATIONALITY = "BRASILEIRA";
  private static final String CREATED_PATIENT_NAME = "Maria Teste";
  private static final String UPDATED_PATIENT_NAME = "V019 ATUALIZADO";
  private static final String PATH_SEPARATOR = "/";
  private static final Long PATIENT_ID_1002 = 1002L;
  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private ObjectMapper objectMapper;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).build();
  }

  @Test
  @DisplayName("Given valid patient when creating patient then return auditing data")
  void givenValidPatient_whenCreatingPatient_thenReturnAuditingData() throws Exception {
    Patient patient = setupGivenValidPatientWhenCreatingPatientThenReturnAuditingData();
    String body = toJson(patient);

    mockMvc.perform(post(PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.createdBy")
            .value(USERNAME))
        .andExpect(jsonPath("$.createdAt").isNotEmpty());
  }

  private Patient setupGivenValidPatientWhenCreatingPatientThenReturnAuditingData() {
    Patient patient = new Patient();
    patient.setName(CREATED_PATIENT_NAME);
    patient.setDocumentNumber("11122233344");
    patient.setSex(Sex.FEMALE);
    patient.setBirthDate(createDate("20/05/1985"));
    patient.setNationality(NATIONALITY);
    patient.setCivilStatus(CivilStatus.SINGLE);
    patient.setJob("ENGENHEIRA");
    return patient;
  }

  private LocalDate createDate(String date) {
    return LocalDate.parse(date, DATE_FORMAT);
  }

  private String toJson(Object object) {
    return objectMapper.writeValueAsString(object);
  }

  @Test
  @DisplayName("Given existing patient when updating patient then return updated by")
  void givenExistingPatient_whenUpdatingPatient_thenReturnUpdatedBy()
      throws Exception {
    PatientRequest request = setupGivenExistingPatientWhenUpdatingPatientThenReturnUpdatedBy();
    String body = toJson(request);

    mockMvc.perform(put(PATIENT + PATH_SEPARATOR + PATIENT_ID_1002)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME)
            .value(UPDATED_PATIENT_NAME))
        .andExpect(jsonPath("$.updatedBy")
            .value(USERNAME));
  }

  private PatientRequest setupGivenExistingPatientWhenUpdatingPatientThenReturnUpdatedBy() {
    return new PatientRequest(
        UPDATED_PATIENT_NAME,
        "00000000019",
        Sex.MALE,
        createDate("01/01/1991"),
        NATIONALITY,
        CivilStatus.SINGLE,
        "PESQUISADOR");
  }
}
