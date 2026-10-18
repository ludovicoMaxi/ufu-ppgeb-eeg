package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Profession;

/**
 * Service interface for Profession operations.
 */
public interface ProfessionService {

  /**
   * Lists all professions ordered by name.
   *
   * @return the professions
   */
  List<Profession> findAll();

  /**
   * Registers the given profession name in the catalog if it does not exist yet.
   * Blank names are ignored.
   *
   * @param name the profession name
   */
  void registerIfAbsent(String name);
}
