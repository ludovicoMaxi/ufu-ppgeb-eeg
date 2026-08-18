package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Medicament;

/**
 * Service interface for Medicament operations.
 */
public interface MedicamentService {

  /**
   * Finds all medicaments.
   *
   * @return the list of medicaments
   */
  List<Medicament> findAll();

  /**
   * Finds medicaments by name.
   *
   * @param name the medicament name
   * @return the list of medicaments
   */
  List<Medicament> findByName(String name);

  /**
   * Saves a medicament.
   *
   * @param medicament the medicament to save
   * @return the saved medicament
   */
  Medicament save(Medicament medicament);

}
