package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Unit operations.
 */
public interface UnitService {

  /**
   * Finds all units paginated.
   *
   * @param pageable the pagination information
   * @return the page of units
   */
  Page<Unit> findAll(Pageable pageable);

}
