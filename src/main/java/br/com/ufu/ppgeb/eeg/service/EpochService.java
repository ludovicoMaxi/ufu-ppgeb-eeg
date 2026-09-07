package br.com.ufu.ppgeb.eeg.service;

import java.util.List;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for Epoch operations.
 */
public interface EpochService {

  /**
   * Saves an epoch.
   *
   * @param epoch the epoch to save
   * @return the saved epoch
   */
  Epoch save(Epoch epoch);

  /**
   * Finds an epoch by id.
   *
   * @param id the epoch id
   * @return the epoch
   */
  Epoch findById(Long id);

  /**
   * Finds epochs by exam id paginated.
   *
   * @param examId the exam id
   * @param pageable the pagination information
   * @return the page of epochs
   */
  Page<Epoch> findByExamId(Long examId, Pageable pageable);

  /**
   * Finds all epochs.
   *
   * @return the list of epochs
   */
  List<Epoch> findAll();

  /**
   * Deletes an epoch by id.
   *
   * @param id the epoch id
   */
  void delete(Long id);

  /**
   * Updates a list of epochs of an exam.
   *
   * @param examId the exam id
   * @param epochs the epochs to update
   * @return the updated list of epochs
   */
  List<Epoch> updateList(Long examId, List<Epoch> epochs);

}
