package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Unit;
import java.util.List;

/**
 * Service interface for Unit operations.
 */
public interface UnitService {

  /**
   * Finds all units.
   *
   * @return the list of units
   */
  List<Unit> findAll();

}
