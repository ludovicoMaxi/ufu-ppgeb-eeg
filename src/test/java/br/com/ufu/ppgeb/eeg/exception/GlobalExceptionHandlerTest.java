package br.com.ufu.ppgeb.eeg.exception;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import br.com.ufu.ppgeb.eeg.controller.PatientController;
import br.com.ufu.ppgeb.eeg.dto.PatientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class GlobalExceptionHandlerTest {

  private static final String BINDING_OBJECT_NAME = "request";
  private static final String NOT_FOUND_MESSAGE = "Paciente não encontrado(a) com id=999";
  private static final String VALIDATION_MESSAGE = "name must not be blank";
  private static final Long NOT_FOUND_ID = 999L;

  private GlobalExceptionHandler exceptionHandler;

  @BeforeEach
  void setUp() {
    exceptionHandler = new GlobalExceptionHandler();
  }

  @Test
  @DisplayName("Given validation error when handling validation then return bad request with field message")
  void givenValidationError_whenHandlingValidation_thenReturnBadRequestWithFieldMessage()
      throws Exception {
    MethodArgumentNotValidException ex =
        setupGivenValidationErrorWhenHandlingValidationThenReturnBadRequestWithFieldMessage();

    ResponseEntity<ProblemDetail> response =
        exceptionHandler.handleValidation(ex);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo(VALIDATION_MESSAGE);
  }

  private MethodArgumentNotValidException
      setupGivenValidationErrorWhenHandlingValidationThenReturnBadRequestWithFieldMessage()
          throws Exception {
    Method method = PatientController.class.getMethod("save", PatientRequest.class,
        String.class);
    MethodParameter parameter = new MethodParameter(method, 0);
    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(new Object(), BINDING_OBJECT_NAME);
    bindingResult.addError(new FieldError(BINDING_OBJECT_NAME, "name", "must not be blank"));
    return new MethodArgumentNotValidException(parameter, bindingResult);
  }

  @Test
  @DisplayName("Given resource not found when handling not found then return not found with message")
  void givenResourceNotFound_whenHandlingNotFound_thenReturnNotFoundWithMessage() {
    ResourceNotFoundException exception = new ResourceNotFoundException("Paciente", NOT_FOUND_ID);

    ResponseEntity<ProblemDetail> response =
        exceptionHandler.handleNotFound(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getDetail()).isEqualTo(NOT_FOUND_MESSAGE);
  }
}
