package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Medicament;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Medicament entities.
 */
public interface MedicamentRepository
    extends JpaRepository<Medicament, Long> {

  /**
   * Finds medicaments by name.
   *
   * @param name the medicament name
   * @return the list of medicaments
   */
  List<Medicament> findByName(String name);

  /**
   * Checks if medicament exists by name.
   *
   * @param name the medicament name
   * @return true if exists
   */
  boolean existsByName(String name);

}
