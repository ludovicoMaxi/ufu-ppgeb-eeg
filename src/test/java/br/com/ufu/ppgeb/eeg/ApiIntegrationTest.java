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
    mockMvc.perform(get("/api/unit"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldListUnits() throws Exception {
    mockMvc.perform(
            get("/api/unit").with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3))
        .andExpect(jsonPath("$[0].name").value("mg"));
  }

  @Test
  void shouldFindPatientById() throws Exception {
    mockMvc.perform(get("/api/patient/1001")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name")
            .value("JOAO LUDOVICO"));
  }

  @Test
  void shouldReturnNotFoundForUnknownPatient()
      throws Exception {
    mockMvc.perform(get("/api/patient/999999")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldRejectPatientSearchWithoutFilter()
      throws Exception {
    mockMvc.perform(get("/api/patient")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldSearchPatientByDocumentNumber()
      throws Exception {
    mockMvc.perform(get("/api/patient")
            .param("documentNumber", "09574539652")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].name")
            .value("JOAO LUDOVICO"));
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

    mockMvc.perform(post("/api/patient")
            .with(httpBasic("joaol", "123"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNumber())
        .andExpect(jsonPath("$.createdBy")
            .value("joaol"))
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

    mockMvc.perform(post("/api/patient")
            .with(httpBasic("joaol", "123"))
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

    mockMvc.perform(put("/api/patient")
            .with(httpBasic("joaol", "123"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name")
            .value("V019 ATUALIZADO"))
        .andExpect(jsonPath("$.updatedBy")
            .value("joaol"));
  }

  @Test
  void shouldLoadExamWithNestedCollections()
      throws Exception {
    mockMvc.perform(get("/api/exam/1001")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.patient.name")
            .value("JOAO LUDOVICO"))
        .andExpect(jsonPath(
            "$.examMedicaments[0].amount").value(50))
        .andExpect(jsonPath(
            "$.examMedicaments[0].medicament.name")
            .value("DIPIRONA"))
        .andExpect(jsonPath(
            "$.examEquipments[0].equipment.name")
            .value("BRAINVISIAN"));
  }

  @Test
  void shouldSearchExamByPatient() throws Exception {
    mockMvc.perform(get("/api/exam")
            .param("patientId", "1001")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath(
            "$[0].examMedicaments[0].amount").value(50));
  }

  @Test
  void shouldListEpochsByExam() throws Exception {
    mockMvc.perform(get("/api/epoch")
            .param("examId", "1001")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].description")
            .value("Em Silencio"));
  }

  @Test
  void shouldListActivitiesByExam() throws Exception {
    mockMvc.perform(get("/api/activity")
            .param("examId", "1001")
            .with(httpBasic("joaol", "123")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].description")
            .value("Repouso"));
  }
}
