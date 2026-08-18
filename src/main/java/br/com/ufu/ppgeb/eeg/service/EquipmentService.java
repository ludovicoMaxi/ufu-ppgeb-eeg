package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Equipment;

/**
 * Service interface for Equipment operations.
 */
public interface EquipmentService {

  /**
   * Finds all equipment.
   *
   * @return the list of equipment
   */
  List<Equipment> findAll();

  /**
   * Finds equipment by name.
   *
   * @param name the equipment name
   * @return the list of equipment
   */
  List<Equipment> findByName(String name);

  /**
   * Saves an equipment.
   *
   * @param equipment the equipment to save
   * @return the saved equipment
   */
  Equipment save(Equipment equipment);

}
