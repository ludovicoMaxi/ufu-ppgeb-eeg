package br.com.ufu.ppgeb.eeg;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
  private static final String EXAM_ID_PARAM = "examId";

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity()).build();
  }

  @Test
  void shouldRejectUnauthenticatedRequest() throws Exception {
    mockMvc.perform(get(API_UNIT))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldListUnits() throws Exception {
    mockMvc.perform(get(API_UNIT).with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(LENGTH))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME).value("mg"));
  }

  @Test
  void shouldFindPatientById() throws Exception {
    mockMvc.perform(get(API_PATIENT + "/" + PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME)
            .value(PATIENT_NAME));
  }

  @Test
  void shouldReturnNotFoundForUnknownPatient()
      throws Exception {
    mockMvc.perform(get(API_PATIENT + "/999999")
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldRejectPatientSearchWithoutFilter()
      throws Exception {
    mockMvc.perform(get(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldSearchPatientByDocumentNumber()
      throws Exception {
    mockMvc.perform(get(API_PATIENT)
            .param("documentNumber", "09574539652")
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_NAME)
            .value(PATIENT_NAME));
  }

  @Test
  void shouldCreatePatientWithAuditing() throws Exception {
    String body =
        """
        {
          "name": "Maria Teste",
          "documentNumber": "11122233344",
          "sex": "F",
          "birthDate": "20/05/1985",
          "nacionality": "BRASILEIRA",
          "civilStatus": "SOLTEIRA",
          "job": "ENGENHEIRA"
        }
        """;

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

  @Test
  void shouldRejectDuplicatedDocumentNumber()
      throws Exception {
    String body =
        """
        {
          "name": "Duplicado",
          "documentNumber": "09574539652",
          "sex": "M",
          "birthDate": "01/01/1990",
          "nacionality": "BRASILEIRA"
        }
        """;

    mockMvc.perform(post(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdatePatientAndSetUpdatedBy()
      throws Exception {
    String body =
        """
        {
          "id": 1002,
          "name": "V019 ATUALIZADO",
          "documentNumber": "00000000019",
          "sex": "M",
          "birthDate": "01/01/1991",
          "nacionality": "BRASILEIRA",
          "civilStatus": "SOLTEIRO",
          "job": "PESQUISADOR"
        }
        """;

    mockMvc.perform(put(API_PATIENT)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_NAME)
            .value("V019 ATUALIZADO"))
        .andExpect(jsonPath("$.updatedBy")
            .value(USERNAME));
  }

  @Test
  void shouldLoadExamWithNestedCollections()
      throws Exception {
    mockMvc.perform(get("/api/exam/" + PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.patient.name")
            .value(PATIENT_NAME))
        .andExpect(jsonPath("$.examMedicaments[0].amount").value(AMOUNT))
        .andExpect(jsonPath("$.examMedicaments[0].medicament.name")
            .value("DIPIRONA"))
        .andExpect(jsonPath("$.examEquipments[0].equipment.name")
            .value("BRAINVISIAN"));
  }

  @Test
  void shouldSearchExamByPatient() throws Exception {
    mockMvc.perform(get("/api/exam")
            .param("patientId", PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath("$[0].examMedicaments[0].amount").value(AMOUNT));
  }

  @Test
  void shouldListEpochsByExam() throws Exception {
    mockMvc.perform(get("/api/epoch")
            .param(EXAM_ID_PARAM, PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_DESC)
            .value("Em Silencio"));
  }

  @Test
  void shouldListActivitiesByExam() throws Exception {
    mockMvc.perform(get("/api/activity")
            .param(EXAM_ID_PARAM, PATIENT_ID_1001)
            .with(httpBasic(USERNAME, PASSWORD)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(JSON_PATH_LENGTH).value(1))
        .andExpect(jsonPath(JSON_PATH_FIRST_DESC)
            .value("Repouso"));
  }
}
