package br.com.ufu.ppgeb.eeg.exception;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Builds an RFC 9457 ProblemDetail for the given status and message.
   *
   * @param status the HTTP status
   * @param message the detail message
   * @return the problem detail
   */
  private static ProblemDetail problemDetail(HttpStatus status, String message) {

    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, message);
    problem.setTitle(status.getReasonPhrase());
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }

  /**
   * Handles ResourceNotFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(problemDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  /**
   * Handles EntityNotFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
  public ResponseEntity<ProblemDetail> handleEntityNotFound(
      jakarta.persistence.EntityNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(problemDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
  }

  /**
   * Handles NoResourceFoundException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ProblemDetail> handleMissingResource(NoResourceFoundException ex) {

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(problemDetail(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
  }

  /**
   * Handles IllegalArgumentException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> handleIllegalArgument(IllegalArgumentException ex) {

    return ResponseEntity.badRequest()
        .body(problemDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  /**
   * Handles HttpMessageNotReadableException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ProblemDetail> handleUnreadableMessage(HttpMessageNotReadableException ex) {

    logger.warn("Requisição com corpo inválido: {}", ex.getMostSpecificCause().getMessage());
    return ResponseEntity.badRequest()
        .body(problemDetail(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido."));
  }

  /**
   * Handles bean validation failures.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException ex) {

    String message = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> error.getField() + " " + error.getDefaultMessage())
        .orElse("Requisição inválida.");
    return ResponseEntity.badRequest()
        .body(problemDetail(HttpStatus.BAD_REQUEST, message));
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
  public ResponseEntity<ProblemDetail> handleInvalidRequest(Exception ex) {

    return ResponseEntity.badRequest()
        .body(problemDetail(HttpStatus.BAD_REQUEST, ex.getMessage()));
  }

  /**
   * Handles DataIntegrityViolationException.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ProblemDetail> handleDataIntegrity(DataIntegrityViolationException ex) {

    logger.warn("Violação de integridade de dados", ex);
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(problemDetail(HttpStatus.CONFLICT,
            "Registro conflitante ou referenciado por outro cadastro."));
  }

  /**
   * Handles unexpected exceptions.
   *
   * @param ex the exception
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ProblemDetail> handleUnexpected(Exception ex) {

    logger.error("Erro inesperado ao processar requisição", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(problemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno ao processar a requisição."));
  }
}