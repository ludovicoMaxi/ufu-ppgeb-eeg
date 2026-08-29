package br.com.ufu.ppgeb.eeg;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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

  public static final int LENGTH = 3;
  public static final int AMOUNT = 50;
  private static final String API_UNIT = "/api/unit";
  private static final String API_PATIENT = "/api/patient";
  private static final String USERNAME = "joaol";
  private static final String PASSWORD = "123";
  private static final String JSON_PATH_LENGTH = "$.length()";
  private static final String JSON_PATH_FIRST_NAME = "$[0].name";
  private static final String JSON_PATH_NAME = "$.name";
  private static final String JSON_PATH_FIRST_DESC = "$[0].description";
  private static final String PATIENT_NAME = "JOAO LUDOVICO";
  private static final String PATIENT_ID_1001 = "1001";
  private static final Long PATIENT_ID_1002 = 1002L;
  private static final String EXAM_ID_PARAM = "examId";
  private static final String DUPLICATED_DOCUMENT_NUMBER = "09574539652";
  private static final String NATIONALITY = "BRASILEIRA";
  private static final String CREATED_PATIENT_NAME = "Maria Teste";
  private static final String DUPLICATED_PATIENT_NAME = "Duplicado";
  private static final String UPDATED_PATIENT_NAME = "V019 ATUALIZADO";
  private static final String MEDICAMENT_NAME = "DIPIRONA";
  private static final String EQUIPMENT_NAME = "BRAINVISIAN";
  private static final String EPOCH_DESCRIPTION = "Em Silencio";
  private static final String ACTIVITY_DESCRIPTION = "Repouso";
  private static final String PATH_SEPARATOR = "/";
  private static final String NEW_PATIENT_DOCUMENT = "22233344455";
  private static final String NEW_PATIENT_BIRTH_DATE = "10/10/1992";
  private static final CivilStatus NEW_PATIENT_CIVIL_STATUS = CivilStatus.MARRIED;
  private static final String NEW_PATIENT_JOB = "MEDICO";
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
  @DisplayName("Given unauthenticated request when accessing units then return unauthorized")
  void givenUnauthenticatedRequest_whenAccessingUnits_thenReturnUnauthorized() throws Exception {
    mockMvc.perform(get(API_UNIT))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @DisplayName("Given valid credentials when finding all units then return units")
  void givenValidCredentials_whenFindingAllUnits_thenReturnUnits() throws Exception {
    mockMvc.perform(get(API_UNIT).with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(LENGTH))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value("mg"));
  }

  @Test
  @DisplayName("Given existing patient ID when finding patient then return patient")
  void givenExistingPatientId_whenFindingPatient_thenReturnPatient() throws Exception {
    mockMvc.perform(get(API_PATIENT + PATH_SEPARATOR + PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME)
            .value(PATIENT_NAME));
  }

  @Test
  @DisplayName("Given unknown patient ID when finding patient then return not found")
  void givenUnknownPatientId_whenFindingPatient_thenReturnNotFound()
      throws Exception {
    mockMvc.perform(get(API_PATIENT + "/999999")
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Given missing patient filter when searching patients then return bad request")
  void givenMissingPatientFilter_whenSearchingPatients_thenReturnBadRequest()
      throws Exception {
    mockMvc.perform(get(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("Given patient document number when searching patients then return matching patient")
  void givenPatientDocumentNumber_whenSearchingPatients_thenReturnMatchingPatient()
      throws Exception {
    mockMvc.perform(get(API_PATIENT)
            .param("documentNumber", DUPLICATED_DOCUMENT_NUMBER)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME)
            .value(PATIENT_NAME));
  }

  @Test
  @DisplayName("Given valid patient when creating patient then return auditing data")
  void givenValidPatient_whenCreatingPatient_thenReturnAuditingData() throws Exception {
    Patient patient = setupGivenValidPatientWhenCreatingPatientThenReturnAuditingData();
    String body = toJson(patient);

    mockMvc.perform(post(API_PATIENT)
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
  @DisplayName("Given duplicated document number when creating patient then return bad request")
  void givenDuplicatedDocumentNumber_whenCreatingPatient_thenReturnBadRequest()
      throws Exception {
    Patient patient = setupGivenDuplicatedDocumentNumberWhenCreatingPatientThenReturnBadRequest();
    String body = toJson(patient);

    mockMvc.perform(post(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  private Patient setupGivenDuplicatedDocumentNumberWhenCreatingPatientThenReturnBadRequest() {
    Patient patient = new Patient();
    patient.setName(DUPLICATED_PATIENT_NAME);
    patient.setDocumentNumber(DUPLICATED_DOCUMENT_NUMBER);
    patient.setSex(Sex.MALE);
    patient.setBirthDate(createDate("01/01/1990"));
    patient.setNationality(NATIONALITY);
    return patient;
  }

  @Test
  @DisplayName("Given existing patient when updating patient then return updated by")
  void givenExistingPatient_whenUpdatingPatient_thenReturnUpdatedBy()
      throws Exception {
    PatientRequest request = setupGivenExistingPatientWhenUpdatingPatientThenReturnUpdatedBy();
    String body = toJson(request);

    mockMvc.perform(put(API_PATIENT + PATH_SEPARATOR + PATIENT_ID_1002)
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

  @Test
  @DisplayName("Given valid patient when creating patient then return location header")
  void givenValidPatient_whenCreatingPatient_thenReturnLocationHeader() throws Exception {
    PatientRequest request = setupGivenValidPatientWhenCreatingPatientThenReturnLocationHeader();

    mockMvc.perform(post(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(request)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", startsWith(API_PATIENT + PATH_SEPARATOR)));
  }

  private PatientRequest setupGivenValidPatientWhenCreatingPatientThenReturnLocationHeader() {
    return new PatientRequest("Carlos Teste", NEW_PATIENT_DOCUMENT, Sex.MALE,
        createDate(NEW_PATIENT_BIRTH_DATE), NATIONALITY, NEW_PATIENT_CIVIL_STATUS,
        NEW_PATIENT_JOB);
  }

  @Test
  @DisplayName("Given patient with missing required field when creating patient then return bad request")
  void givenPatientMissingRequiredField_whenCreatingPatient_thenReturnBadRequest()
      throws Exception {
    PatientRequest request = setupGivenPatientMissingRequiredFieldWhenCreatingPatientThenReturnBadRequest();

    mockMvc.perform(post(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJson(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("name must not be blank"));
  }

  private PatientRequest setupGivenPatientMissingRequiredFieldWhenCreatingPatientThenReturnBadRequest() {
    return new PatientRequest(null, NEW_PATIENT_DOCUMENT, Sex.MALE,
        createDate(NEW_PATIENT_BIRTH_DATE), NATIONALITY, NEW_PATIENT_CIVIL_STATUS,
        NEW_PATIENT_JOB);
  }

  @Test
  @DisplayName("Given exam with nested collections when finding exam then return nested data")
  void givenExamWithNestedCollections_whenFindingExam_thenReturnNestedData()
      throws Exception {
    mockMvc.perform(get("/api/exam/" + PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.patient.name")
            .value(PATIENT_NAME))
        .andExpect(jsonPath("$.examMedicaments[0].amount").value(AMOUNT))
        .andExpect(jsonPath("$.examMedicaments[0].medicament.name")
            .value(MEDICAMENT_NAME))
        .andExpect(jsonPath("$.examEquipments[0].equipment.name")
            .value(EQUIPMENT_NAME));
  }

  @Test
  @DisplayName("Given patient with exam when searching exams then return exam")
  void givenPatientWithExam_whenSearchingExams_thenReturnExam() throws Exception {
    mockMvc.perform(get("/api/exam")
            .param("patientId", PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$[0].examMedicaments[0].amount").value(AMOUNT));
  }

  @Test
  @DisplayName("Given exam ID when listing epochs then return epochs")
  void givenExamId_whenListingEpochs_thenReturnEpochs() throws Exception {
    mockMvc.perform(get("/api/epoch")
            .param(EXAM_ID_PARAM, PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_DESC)
            .value(EPOCH_DESCRIPTION));
  }

  @Test
  @DisplayName("Given exam ID when listing activities then return activities")
  void givenExamId_whenListingActivities_thenReturnActivities() throws Exception {
    mockMvc.perform(get("/api/activity")
            .param(EXAM_ID_PARAM, PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_DESC)
            .value(ACTIVITY_DESCRIPTION));
  }
}
