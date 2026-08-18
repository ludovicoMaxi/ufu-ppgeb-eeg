package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Unit entities.
 */
public interface UnitRepository extends JpaRepository<Unit, Long> {

}
