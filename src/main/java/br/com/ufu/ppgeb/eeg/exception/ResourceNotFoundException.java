package br.com.ufu.ppgeb.eeg.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a resource is not found.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new ResourceNotFoundException.
   *
   * @param resource the resource name
   * @param id the resource id
   */
  public ResourceNotFoundException(String resource, Long id) {

    super(resource + " não encontrado(a) com id=" + id);
  }
}
