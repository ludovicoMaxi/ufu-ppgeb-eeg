package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Unit;

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
