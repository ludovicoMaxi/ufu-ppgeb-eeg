package br.com.ufu.ppgeb.eeg.service;

import br.com.ufu.ppgeb.eeg.model.Epoch;
import br.com.ufu.ppgeb.eeg.view.EpochList;
import java.util.List;

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
   * Finds epochs by filter.
   *
   * @param examId the exam id
   * @return the list of epochs
   */
  List<Epoch> findByFilter(Long examId);

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
   * Updates a list of epochs.
   *
   * @param epochList the epoch list
   * @return the updated list
   */
  List<Epoch> updateList(EpochList epochList);

}
