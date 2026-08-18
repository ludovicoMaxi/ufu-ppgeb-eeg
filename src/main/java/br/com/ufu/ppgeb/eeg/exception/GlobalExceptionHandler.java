package br.com.ufu.ppgeb.eeg.exception;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for REST API.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger =
      LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Represents an API error response.
   */
  public record ApiError(Instant timestamp, int status, String error, String message) {

    static ApiError of(HttpStatus status, String message) {

      return new ApiError(Instant.now(), status.value(),
          status.getReasonPhrase(), message);
    }
  }

  /**
   * Handles ResourceNotFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  /**
   * Handles EntityNotFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
  public ResponseEntity<ApiError> handleEntityNotFound(
      jakarta.persistence.EntityNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiError.of(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  /**
   * Handles NoResourceFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ApiError> handleMissingResource(NoResourceFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(ApiError.of(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
  }

  /**
   * Handles IllegalArgumentException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {

    return ResponseEntity.badRequest()
        .body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  /**
   * Handles HttpMessageNotReadableException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleUnreadableMessage(HttpMessageNotReadableException ex) {

    logger.warn("Requisição com corpo inválido: {}", ex.getMostSpecificCause().getMessage());
    return ResponseEntity.badRequest()
        .body(ApiError.of(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido."));
  }

  /**
   * Handles missing or invalid request parameters.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler({
      MissingServletRequestParameterException.class,
      MethodArgumentTypeMismatchException.class
  })
  public ResponseEntity<ApiError> handleInvalidRequest(Exception ex) {

    return ResponseEntity.badRequest()
        .body(ApiError.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  /**
   * Handles DataIntegrityViolationException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex) {

    logger.warn("Violação de integridade de dados", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(ApiError.of(HttpStatus.CONFLICT,
            "Registro conflitante ou referenciado por outro cadastro."));
  }

  /**
   * Handles unexpected exceptions.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleUnexpected(Exception ex) {

    logger.error("Erro inesperado ao processar requisição", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno ao processar a requisição."));
  }
}
