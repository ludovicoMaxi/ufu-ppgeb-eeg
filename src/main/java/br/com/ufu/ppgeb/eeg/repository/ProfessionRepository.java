package br.com.ufu.ppgeb.eeg.repository;

import java.util.List;
import java.util.Optional;

import br.com.ufu.ppgeb.eeg.model.Profession;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Profession entities.
 */
public interface ProfessionRepository extends JpaRepository<Profession, Long> {

  /**
   * Finds all professions ordered by name.
   *
   * @return the professions ordered by name
   */
  List<Profession> findAllByOrderByNameAsc();

  /**
   * Finds a profession by its exact (normalized) name.
   *
   * @param name the profession name
   * @return the profession, if present
   */
  Optional<Profession> findByName(String name);
}
