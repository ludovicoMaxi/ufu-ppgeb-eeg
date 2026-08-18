package br.com.ufu.ppgeb.eeg.repository;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for Epoch entities.
 */
public interface EpochRepository
    extends JpaRepository<Epoch, Long> {

  /**
   * Finds epochs by exam id.
   *
   * @param examId the exam id
   * @return the list of epochs
   */
  List<Epoch> findByExamId(Long examId);

}
