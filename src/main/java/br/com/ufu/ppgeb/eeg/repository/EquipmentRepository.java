package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Equipment entities.
 */
public interface EquipmentRepository
    extends JpaRepository<Equipment, Long> {

  /**
   * Finds equipment by name.
   *
   * @param name the equipment name
   * @return the list of equipment
   */
  List<Equipment> findByName(String name);

  /**
   * Checks if equipment exists by name.
   *
   * @param name the equipment name
   * @return true if exists
   */
  boolean existsByName(String name);

}
